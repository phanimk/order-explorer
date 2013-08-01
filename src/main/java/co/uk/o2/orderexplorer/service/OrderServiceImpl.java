/**
 * 
 */
package co.uk.o2.orderexplorer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import co.uk.o2.orderexplorer.utils.OrderType;

import com.mongodb.DBObject;

/**
 * @author Phani Maddali
 * 
 */

@Service
public class OrderServiceImpl implements OrderService {
	private String collectionName = "orders";

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	@Autowired
	private MongoTemplate mongoTemplate;

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public OrderServiceImpl() {
		super();
	}

	public OrderServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public OrderServiceImpl(MongoTemplate mongoTemplate, String collectionName) {
		this.mongoTemplate = mongoTemplate;
		this.collectionName = collectionName;
	}

	@Override
	public long getTotalOrderCount(String type, String brand, String model,
			Date fromDate, Date toDate) {
		return getOrdersCount(type, brand, model, fromDate, toDate, null);
	}

	@Override
	public long getSuccessfullyRequestedOrderCount(String type, String brand,
			String model, Date fromDate, Date toDate) {
		return getOrdersCount(type, brand, model, fromDate, toDate,
				"SubmissionFailed");
	}

	@Override
	public long getSuccessfullyCompletedOrderCount(String type, String brand,
			String model, Date fromDate, Date toDate) {
		return getOrdersCount(type, brand, model, fromDate, toDate,
				"OrderAccepted");
	}

	@Override
	public long getRejectedOrderCount(String type, String brand, String model,
			Date fromDate, Date toDate) {
		return getOrdersCount(type, brand, model, fromDate, toDate,
				"OrderRejected");
	}
	
	@Override
	public Map<String, List<String>> getAllBrands() {
		HashMap<String, List<String>> brandModelMap = new HashMap<String, List<String>>();
		Query query = new Query();
		query.fields().exclude("_id");
		query.fields().include("product.brand");
		query.fields().include("product.model");
		List<DBObject> results = mongoTemplate.find(query, DBObject.class,
				"products");

		for (DBObject dbObj : results) {
			String brand = (String) ((Map) dbObj.get("product")).get("brand");
			String model = (String) ((Map) dbObj.get("product")).get("model");
			if (brandModelMap.containsKey(brand)) {
				brandModelMap.get(brand).add(model);
			} else {
				List<String> modelList = new ArrayList<String>();
				modelList.add(model);
				brandModelMap.put(brand, modelList);
			}
		}
		return brandModelMap;
	}

	@Override
	public List<String> getAllModelsForBrand(String brand) {
		Query query = new Query(Criteria.where("brand").is(brand));
		query.fields().include("model");
		return mongoTemplate.find(query, String.class);
	}

	private long getOrdersCount(String type, String brand, String model,
			Date fromDate, Date toDate, String action) {
//		String productId = null;
//		Query productQuery = new Query();
//		if (brand != null) {
//			if (model != null) {
//				productQuery.addCriteria(Criteria.where("brand").is(brand)
//						.and("model").is(model));
//				productId = mongoTemplate.findOne(productQuery, String.class);
//			}
//		}

		Query query = new Query();

//		if (productId != null) {
//			query.addCriteria(Criteria.where(
//					"lineItems.fulfillmentData.productId").is(productId));
//		}

		if(type != null) {
			String orderNumRegEx = getOrderNumberRegEx(type);
			if(orderNumRegEx != null) {
				query.addCriteria(Criteria.where("orderNumber").regex(orderNumRegEx));
			}
		}
		
		if(action !=  null) {
			query.addCriteria(Criteria.where("submissionState").is(action));
		}

		Criteria createdTimeCriteria = null;
		if(fromDate!=null) {
			createdTimeCriteria = Criteria.where("createdTime")
					.gt(df.format(fromDate));
		}

		if(toDate != null) {
			if(createdTimeCriteria != null) {
				query.addCriteria(createdTimeCriteria
						.lt(df.format(toDate)));
			} else {
				query.addCriteria(Criteria.where("createdTime")
						.lt(df.format(toDate)));
			}
		}

		System.out.println("Query: "+query);
		return mongoTemplate.count(query, collectionName);
	}
	
	private String getOrderNumberRegEx(String type) {
		String retStr = null;
		if (OrderType.CFU.getName().equals(type)) {
			retStr = "^ms-1";
		} else if (OrderType.AFU.getName().equals(type)) {
			retStr = "^ms-2";
		} else if (OrderType.CFA.getName().equals(type)) {
			retStr = "^ms-3";
		} else if (OrderType.AFA.getName().equals(type)) {
			retStr = "^ms-4";
		}
		
		return retStr;
	}
}
