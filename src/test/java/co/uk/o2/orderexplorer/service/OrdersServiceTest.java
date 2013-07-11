/**
 * 
 */
package co.uk.o2.orderexplorer.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBCollection;

/**
 * @author Phani Maddali
 * 
 */
public class OrdersServiceTest {

	private static final String collectionName = "sampleorders";

	private MongoTemplate mongoT = mock(MongoTemplate.class);

	private DBCollection dbCollection = mock(DBCollection.class);

	private OrderService orderService;

	private static final String CFU = "CFU";
	private static final String AFU = "AFU";
	private static final String CFA = "CFA";
	private static final String AFA = "AFA";

	@Before
	public void setUp() {
		orderService = new OrderServiceImpl(mongoT, collectionName);
		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where(
								"submissionState").is("SubmissionFailed")),
						collectionName)).thenReturn(Long.valueOf(1));
		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where(
								"submissionState").is("OrderRejected")),
						collectionName)).thenReturn(Long.valueOf(1));
		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where(
								"submissionState").is("OrderAccepted")),
						collectionName)).thenReturn(Long.valueOf(1));

		when(
				mongoT.count((new Query()).addCriteria(Criteria.where(
						"orderNumber").regex("ms-1*")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count((new Query()).addCriteria(Criteria.where(
						"orderNumber").regex("ms-2*")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count((new Query()).addCriteria(Criteria.where(
						"orderNumber").regex("ms-3*")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count((new Query()).addCriteria(Criteria.where(
						"orderNumber").regex("ms-4*")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-1*").and("submissionState")
								.is("SubmissionFailed")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-2*").and("submissionState")
								.is("SubmissionFailed")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-3*").and("submissionState")
								.is("SubmissionFailed")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-4*").and("submissionState")
								.is("SubmissionFailed")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-1*").and("submissionState")
								.is("OrderAccepted")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-2*").and("submissionState")
								.is("OrderAccepted")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-3*").and("submissionState")
								.is("OrderAccepted")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-4*").and("submissionState")
								.is("OrderAccepted")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-1*").and("submissionState")
								.is("OrderRejected")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-2*").and("submissionState")
								.is("OrderRejected")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-3*").and("submissionState")
								.is("OrderRejected")), collectionName))
				.thenReturn(Long.valueOf(1));

		when(
				mongoT.count(
						(new Query()).addCriteria(Criteria.where("orderNumber")
								.regex("ms-4*").and("submissionState")
								.is("OrderRejected")), collectionName))
				.thenReturn(Long.valueOf(1));
		when(mongoT.getCollection(collectionName)).thenReturn(dbCollection);
		when(dbCollection.count()).thenReturn(Long.valueOf(3));
	}

	@Test
	public void getTotalOrderCount() {
		Assert.assertEquals(3, orderService.getTotalOrderCount());
	}

	@Test
	public void getSuccessfullyRequestedOrderCount() {
		Assert.assertEquals(1,
				orderService.getSuccessfullyRequestedOrderCount());
	}

	@Test
	public void getSuccessfullyCompletedOrderCount() {
		Assert.assertEquals(1,
				orderService.getSuccessfullyCompletedOrderCount());
	}

	@Test
	public void getRejectedOrderCount() {
		Assert.assertEquals(1, orderService.getRejectedOrderCount());
	}

	@Test
	public void getTotalOrderCountByType() {
		Assert.assertEquals(1, orderService.getTotalOrderCountByType(CFU));
		Assert.assertEquals(1, orderService.getTotalOrderCountByType(AFU));
		Assert.assertEquals(1, orderService.getTotalOrderCountByType(CFA));
		Assert.assertEquals(1, orderService.getTotalOrderCountByType(AFA));

	}

	@Test
	public void getSuccessfullyRequestedOrderCountByType() {
		Assert.assertEquals(1,
				orderService.getSuccessfullyRequestedOrderCountByType(CFU));
		Assert.assertEquals(1,
				orderService.getSuccessfullyRequestedOrderCountByType(AFU));
		Assert.assertEquals(1,
				orderService.getSuccessfullyRequestedOrderCountByType(CFA));
		Assert.assertEquals(1,
				orderService.getSuccessfullyRequestedOrderCountByType(AFA));
	}

	@Test
	public void getSuccessfullyCompletedOrderCountByType() {
		Assert.assertEquals(1,
				orderService.getSuccessfullyCompletedOrderCountByType(CFU));
		Assert.assertEquals(1,
				orderService.getSuccessfullyCompletedOrderCountByType(AFU));
		Assert.assertEquals(1,
				orderService.getSuccessfullyCompletedOrderCountByType(CFA));
		Assert.assertEquals(1,
				orderService.getSuccessfullyCompletedOrderCountByType(AFA));
	}

	@Test
	public void getRejectedOrderCountByType() {
		Assert.assertEquals(1, orderService.getRejectedOrderCountByType(CFU));
		Assert.assertEquals(1, orderService.getRejectedOrderCountByType(AFU));
		Assert.assertEquals(1, orderService.getRejectedOrderCountByType(CFA));
		Assert.assertEquals(1, orderService.getRejectedOrderCountByType(AFA));
	}

	/*
	 * public void getTotalOrderCountByMake() {
	 * 
	 * }
	 * 
	 * public void getSuccessfullyRequestedOrderCountByMake() {
	 * 
	 * }
	 * 
	 * public void getSuccessfullyCompletedOrderCountByMake() {
	 * 
	 * }
	 * 
	 * public void getRejectedOrderCountByMake(String make) {
	 * 
	 * }
	 */

}
