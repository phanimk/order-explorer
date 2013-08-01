package co.uk.o2.orderexplorer.service

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ContextConfiguration

import spock.lang.Shared

import com.mongodb.util.JSON

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

@ContextConfiguration("classpath:int-test-orderexplorer.xml")
class OrdersServiceIntTest extends spock.lang.Specification {
	@Value('${mongodb.dbname}')
	String DBNAME;
	
	@Value('${mongodb.port}')
	int port;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	OrderService orderService;
	
	@Shared
	bStartMongoDbSuccess = false;
	
	@Shared
	bInsertData = false;
	
	@Shared
	MongodExecutable mongodExecutable = null;
	
	@Shared
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	def cleanupSpec() {
		if(mongodExecutable != null) {
			mongodExecutable.stop();
		}
	}
	
	def setup() {
		if(!bStartMongoDbSuccess) {
			startMongoDb()
		}
		
		if(!bInsertData) {
			insertData();
		}
	}
	
	def startMongoDb() {
		try {
			IMongodConfig mongodConfig = new MongodConfigBuilder()
			.version(Version.Main.PRODUCTION)
			.net(new Net(port, Network.localhostIsIPv6()))
			.build();
			
			MongodStarter runtime = MongodStarter.getDefaultInstance();
			
			mongodExecutable = runtime.prepare(mongodConfig);
			mongodExecutable.start();
			bStartMongoDbSuccess = true;
		} catch (Exception e) {
			cleanupSpec();
			bStartMongoDbSuccess = false;
		}
	}
	
	def insertData() {
		try {
			mongoTemplate.createCollection("orders");
			mongoTemplate.createCollection("products");
			
			String[] extensions = ["json"]
			Collection<File> filesList = FileUtils.listFiles(context.getResource("data").getFile(), extensions, false);
			for(File file : filesList) {
				String collectionName = file.getName().startsWith("order")?"orders":"products";
				String fileStr = IOUtils.toString(new FileInputStream(file), "UTF-8");
				mongoTemplate.save(JSON.parse(fileStr), collectionName);
			}
			
			bInsertData = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsertedJsonDocsCount() {
		checkDataInsertion();
		Query query = new Query();
		assertEquals(3, mongoTemplate.count(query, "orders"));
		assertEquals(4, mongoTemplate.count(query, "products"));
	}
	
	
	def "Testing orderService.getTotalOrderCount() with different combinations"() {
		when:
			def ordersCount = orderService.getTotalOrderCount(orderType, brand, model, fromDate, toDate)
		then:
			ordersCount == expectedOrdersCount
		where:
			orderType | brand | model | fromDate | toDate | expectedOrdersCount
			null	  | null  | null  | null     | null   | 3
			null	  | null  | null  | df.parse("2013-04-25T17:46:48.884Z")     | null   | 3
	}
	
	def checkDataInsertion() {
		if(!bStartMongoDbSuccess || !bInsertData) {
			fail("Data Insertion Failed.")
		}
	}
}
