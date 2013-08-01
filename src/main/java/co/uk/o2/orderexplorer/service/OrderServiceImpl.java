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

import com.mongodb.DBObject;

/**
 * @author Phani Maddali
 * 
 */

@Service
public class OrderServiceImpl implements OrderService {

	private static final String CFU = "CFU";
	private static final String AFU = "AFU";
	private static final String CFA = "CFA";
	private static final String AFA = "AFA";

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

	public OrderServiceImpl(MongoTemplate mongoTemplate, String collectionName) {
		this.mongoTemplate = mongoTemplate;
		this.collectionName = collectionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#getTotalOrderCount()
	 */
	@Override
	public long getTotalOrderCount() {
		return mongoTemplate.getCollection(collectionName).count();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyRequestedOrderCount()
	 */
	@Override
	public long getSuccessfullyRequestedOrderCount() {
		Query query = new Query();
		query.addCriteria(Criteria.where("submissionState").is(
				"SubmissionFailed"));
		return mongoTemplate.count(query, collectionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyCompletedOrderCount()
	 */
	@Override
	public long getSuccessfullyCompletedOrderCount() {
		Query query = new Query();
		query.addCriteria(Criteria.where("submissionState").is("OrderAccepted"));
		return mongoTemplate.count(query, collectionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#getRejectedOrderCount()
	 */
	@Override
	public long getRejectedOrderCount() {
		Query query = new Query();
		query.addCriteria(Criteria.where("submissionState").is("OrderRejected"));
		return mongoTemplate.count(query, collectionName);
	}

	private long getOrdersCountByType(String type, String action) {
		Query query = new Query();
		if (action == null) {
			if (CFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-1"));
			} else if (AFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-2"));
			} else if (CFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-3"));
			} else if (AFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-4"));
			}
		} else {
			if (CFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-1")
						.and("submissionState").is(action));
			} else if (AFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-2")
						.and("submissionState").is(action));
			} else if (CFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-3")
						.and("submissionState").is(action));
			} else if (AFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-4")
						.and("submissionState").is(action));
			} else {
				query.addCriteria(Criteria.where("submissionState").is(action));
			}
		}
		return mongoTemplate.count(query, collectionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.uk.o2.orderexplorer.service.OrdersService#getTotalOrderCountBytype()
	 */
	@Override
	public long getTotalOrderCountByType(String type, Date fromDate, Date toDate) {
		return getOrdersCount(type, null, null, fromDate, toDate, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyRequestedOrderCountBytype()
	 */
	@Override
	public long getSuccessfullyRequestedOrderCountByType(String type,
			Date fromDate, Date toDate) {
		return getOrdersCount(type, null, null, fromDate, toDate,
				"SubmissionFailed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyCompletedOrderCountBytype()
	 */
	@Override
	public long getSuccessfullyCompletedOrderCountByType(String type,
			Date fromDate, Date toDate) {
		return getOrdersCount(type, null, null, fromDate, toDate,
				"OrderAccepted");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.uk.o2.orderexplorer.service.OrdersService#getRejectedOrderCountBytype
	 * ()
	 */
	@Override
	public long getRejectedOrderCountByType(String type, Date fromDate,
			Date toDate) {
		return getOrdersCount(type, null, null, fromDate, toDate,
				"OrderRejected");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.uk.o2.orderexplorer.service.OrdersService#getTotalOrderCountByMake()
	 */
	@Override
	public long getTotalOrderCountByMake(String make) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyRequestedOrderCountByMake()
	 */
	@Override
	public long getSuccessfullyRequestedOrderCountByMake(String make) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyCompletedOrderCountByMake()
	 */
	@Override
	public long getSuccessfullyCompletedOrderCountByMake(String make) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.uk.o2.orderexplorer.service.OrdersService#getRejectedOrderCountByMake
	 * (java.lang.String)
	 */
	@Override
	public long getRejectedOrderCountByMake(String make) {
		// TODO Auto-generated method stub
		return 0;
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
		String productId = null;
		Query productQuery = new Query();
		if (brand != null) {
			if (model != null) {
				productQuery.addCriteria(Criteria.where("brand").is(brand)
						.and("model").is(model));
				productId = mongoTemplate.findOne(productQuery, String.class);
			}
		}

		Query query = new Query();

		if (productId != null) {
			query.addCriteria(Criteria.where(
					"lineItems.fulfillmentData.productId").is(productId));
		}

		if (action == null) {
			if (CFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-1"));
			} else if (AFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-2"));
			} else if (CFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-3"));
			} else if (AFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-4"));
			}
		} else {
			if (CFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-1")
						.and("submissionState").is(action));
			} else if (AFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-2")
						.and("submissionState").is(action));
			} else if (CFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-3")
						.and("submissionState").is(action));
			} else if (AFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("^ms-4")
						.and("submissionState").is(action));
			} else {
				query.addCriteria(Criteria.where("submissionState").is(action));
			}
		}
		// query.addCriteria(Criteria.where("submittedTime")
		// .gt(fromDate)).lt(toDate)));

		query.addCriteria(Criteria.where("submittedTime")
				.gt(df.format(fromDate)).lt(df.format(toDate)));

		return mongoTemplate.count(query, collectionName);
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
	public Map<String, String> getDailyData(String type, String brand,
			String model, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getWeeklyData(String type, String brand,
			String model, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getMonthlyData(String type, String brand,
			String model, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
