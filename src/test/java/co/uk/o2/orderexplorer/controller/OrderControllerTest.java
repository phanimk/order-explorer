/**
 * 
 */
package co.uk.o2.orderexplorer.controller;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import co.uk.o2.orderexplorer.model.OrderExplorerForm;
import co.uk.o2.orderexplorer.model.OrderStatistics;
import co.uk.o2.orderexplorer.service.OrderService;
import co.uk.o2.orderexplorer.utils.OrderType;

/**
 * @author Phani Maddali
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:test-orderexplorer.xml")
public class OrderControllerTest {

	@Autowired
	private OrderController orderController;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private OrderService mockOrderService;
	
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@After
	public void cleanUp() {
		reset(mockOrderService);
	}

	@Test
	public void testBasicView() {
		OrderExplorerForm expectedExplorerForm = new OrderExplorerForm();
		OrderStatistics expectedOrderStatistics = new OrderStatistics();
		final String URI = "/order-explorer/allproducts";
		
		mockServiceAndPouplateExpectedOrderStatictics(mockOrderService, expectedOrderStatistics);
		
		performActionAndVerify(URI, expectedExplorerForm, expectedOrderStatistics, "main");
	}
	
	private void mockServiceAndPouplateExpectedOrderStatictics(OrderService mockOrderService, 
			OrderStatistics expectedOrderStatistics) {
		final long TOTAL_ORDER_COUNT = 16L;
		final long TOTAL_SUCCESS_COUNT = 4L;
		final long TOTAL_REJECTED_COUNT = 4L;
		final long TOTAL_FAILURE_COUNT = 4L;
		final long TOTAL_ANYORDERTYPE_COUNT = 4L;
		final String PIE_CHART_URL = "/order-explorer/pie-chart.jpeg";
		final String XY_CHART_URL = "/order-explorer/xy-chart.jpeg";
		
		expectedOrderStatistics.setTotalOrderCount(TOTAL_ORDER_COUNT);
		expectedOrderStatistics.setTotalSuccessCount(TOTAL_SUCCESS_COUNT);
		expectedOrderStatistics.setTotalRejectedCount(TOTAL_REJECTED_COUNT);
		expectedOrderStatistics.setTotalFailureCount(TOTAL_FAILURE_COUNT);
		expectedOrderStatistics.setCfaCount(TOTAL_ANYORDERTYPE_COUNT);
		expectedOrderStatistics.setCfuCount(TOTAL_ANYORDERTYPE_COUNT);
		expectedOrderStatistics.setAfaCount(TOTAL_ANYORDERTYPE_COUNT);
		expectedOrderStatistics.setAfuCount(TOTAL_ANYORDERTYPE_COUNT);
		expectedOrderStatistics.setPieChartUrl(PIE_CHART_URL);
		expectedOrderStatistics.setXyChartUrl(XY_CHART_URL);
		
		when(mockOrderService.getTotalOrderCount(null, null, null, null, null)).thenReturn(TOTAL_ORDER_COUNT);
		when(mockOrderService.getSuccessfullyCompletedOrderCount(null, null, null, null, null)).thenReturn(TOTAL_SUCCESS_COUNT);
		when(mockOrderService.getRejectedOrderCount(null, null, null, null, null)).thenReturn(TOTAL_REJECTED_COUNT);
		when(mockOrderService.getSuccessfullyRequestedOrderCount(null, null, null, null, null)).thenReturn(TOTAL_FAILURE_COUNT);
		when(mockOrderService.getTotalOrderCount(eq(OrderType.CFA.getName()), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
			.thenReturn(TOTAL_ANYORDERTYPE_COUNT);
		when(mockOrderService.getTotalOrderCount(eq(OrderType.CFU.getName()), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
		.thenReturn(TOTAL_ANYORDERTYPE_COUNT);
		when(mockOrderService.getTotalOrderCount(eq(OrderType.AFA.getName()), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
		.thenReturn(TOTAL_ANYORDERTYPE_COUNT);
		when(mockOrderService.getTotalOrderCount(eq(OrderType.AFU.getName()), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
		.thenReturn(TOTAL_ANYORDERTYPE_COUNT);
	}
	
	@Test
	public void testProductsView() {
		OrderExplorerForm expectedExplorerForm = new OrderExplorerForm();
		OrderStatistics expectedOrderStatistics = new OrderStatistics();
		
		String requestParamStr = constructReqParamPopExpExpForm(expectedExplorerForm);
		final String URI = "/order-explorer/product?"+requestParamStr;
		
		setExpOrderSerAndPopExpOrdStats(mockOrderService, expectedOrderStatistics, requestParamStr, expectedExplorerForm);
		
		performActionAndVerify(URI, expectedExplorerForm, expectedOrderStatistics, "product");
	}
	
	private String constructReqParamPopExpExpForm(OrderExplorerForm expectedExplorerForm) {
		final String ORDER_TYPE = "CFA";
		final String MAKE = "ALL";
		final String MODEL = "IPHONE";
		final String DATE_TYPE = "CUSTOM";
		final String FROM_DATE = "07/02/2013";
		final String TO_DATE = "07/03/2013";
		
		expectedExplorerForm.setOrderType(ORDER_TYPE);
		expectedExplorerForm.setMake(MAKE);
		expectedExplorerForm.setModel(MODEL);
		expectedExplorerForm.setDate(DATE_TYPE);
		expectedExplorerForm.setFromDate(FROM_DATE);
		expectedExplorerForm.setToDate(TO_DATE);
		
		return "orderType="+ORDER_TYPE+
				"&make="+MAKE+
				"&model="+MODEL+
				"&date="+DATE_TYPE+
				"&fromDate="+FROM_DATE+
				"&toDate="+TO_DATE;
	}
	
	private void setExpOrderSerAndPopExpOrdStats(OrderService mockOrderService, 
			OrderStatistics expectedOrderStatistics,
			String URI, OrderExplorerForm expectedExplorerForm) {
		final long TOTAL_ORDER_COUNT = 4L;
		final long TOTAL_SUCCESS_COUNT = 1L;
		final long TOTAL_REJECTED_COUNT = 1L;
		final long TOTAL_FAILURE_COUNT = 1L;
		final String PIE_CHART_URL = "/order-explorer/pie-chart.jpeg?"+URI;
		final String XY_CHART_URL = "/order-explorer/xy-chart.jpeg?"+URI;
		
		Map<String, Long> customCounts = expectedOrderStatistics.getCustomCounts();
		customCounts.put("OrdersRejected", TOTAL_REJECTED_COUNT);
		customCounts.put("OrdersAccepted", TOTAL_SUCCESS_COUNT);
		customCounts.put("OrdersSubmissionFailed", TOTAL_FAILURE_COUNT);
		expectedOrderStatistics.setTotalOrderCount(TOTAL_ORDER_COUNT);
		expectedOrderStatistics.setProduct(expectedExplorerForm.getOrderType());
		expectedOrderStatistics.setPieChartUrl(PIE_CHART_URL);
		expectedOrderStatistics.setXyChartUrl(XY_CHART_URL);
		
		when(mockOrderService.getTotalOrderCount(anyString(), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class))).thenReturn(TOTAL_ORDER_COUNT);
		when(mockOrderService
				.getRejectedOrderCount(anyString(), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
				.thenReturn(TOTAL_REJECTED_COUNT);
		when(mockOrderService
				.getSuccessfullyCompletedOrderCount(anyString(), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
				.thenReturn(TOTAL_SUCCESS_COUNT);
		when(mockOrderService
				.getSuccessfullyRequestedOrderCount(anyString(), (String)isNull(), (String)isNull(), any(Date.class),any(Date.class)))
				.thenReturn(TOTAL_FAILURE_COUNT);
	}
	
	private void performActionAndVerify(String uri, OrderExplorerForm expectedExplorerForm,
			OrderStatistics expectedOrderStatistics, String viewName) {
		try {
			this.mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(model().attribute("orderExplorerForm", expectedExplorerForm))
			.andExpect(model().attribute("orderStats", expectedOrderStatistics))
			.andExpect(model().attributeExists("brands","models","orderTypes","dateTypes"))
			.andExpect(view().name(viewName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
