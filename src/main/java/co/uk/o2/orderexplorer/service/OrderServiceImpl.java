/**
 * 
 */
package co.uk.o2.orderexplorer.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Phani Maddali
 * 
 */

@Repository
public class OrderServiceImpl implements OrderService {

	private static final String CFU = "CFU";
	private static final String AFU = "AFU";
	private static final String CFA = "CFA";
	private static final String AFA = "AFA";

	private String collectionName;

	private MongoTemplate mongoTemplate;

	public OrderServiceImpl(MongoTemplate mongoT, String collectionName) {
		this.mongoTemplate = mongoT;
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
				query.addCriteria(Criteria.where("orderNumber").regex("ms-1*"));
			} else if (AFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-2*"));
			} else if (CFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-3*"));
			} else if (AFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-4*"));
			}
		} else {
			if (CFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-1*")
						.and("submissionState").is(action));
			} else if (AFU.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-2*")
						.and("submissionState").is(action));
			} else if (CFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-3*")
						.and("submissionState").is(action));
			} else if (AFA.equals(type)) {
				query.addCriteria(Criteria.where("orderNumber").regex("ms-4*")
						.and("submissionState").is(action));
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
	public long getTotalOrderCountByType(String type) {
		return getOrdersCountByType(type, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyRequestedOrderCountBytype()
	 */
	@Override
	public long getSuccessfullyRequestedOrderCountByType(String type) {
		return getOrdersCountByType(type, "SubmissionFailed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.uk.o2.orderexplorer.service.OrdersService#
	 * getSuccessfullyCompletedOrderCountBytype()
	 */
	@Override
	public long getSuccessfullyCompletedOrderCountByType(String type) {
		return getOrdersCountByType(type, "OrderAccepted");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.uk.o2.orderexplorer.service.OrdersService#getRejectedOrderCountBytype
	 * ()
	 */
	@Override
	public long getRejectedOrderCountByType(String type) {
		return getOrdersCountByType(type, "OrderRejected");
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

}
