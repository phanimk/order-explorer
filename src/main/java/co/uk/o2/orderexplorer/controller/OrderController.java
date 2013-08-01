/**
 * 
 */
package co.uk.o2.orderexplorer.controller;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import co.uk.o2.orderexplorer.model.OrderExplorerForm;
import co.uk.o2.orderexplorer.model.OrderStatistics;
import co.uk.o2.orderexplorer.service.OrderService;
import co.uk.o2.orderexplorer.utils.DateType;
import co.uk.o2.orderexplorer.utils.DateUtils;
import co.uk.o2.orderexplorer.utils.OrderType;

/**
 * @author Phani Maddali
 * 
 */

@Controller
@RequestMapping(value = "/order-explorer")
public class OrderController {

	public OrderController() {
		System.out.println("Inside OrderController constructor");
	}
	@Autowired
	@Qualifier(value="orderService")
	private OrderService orderService;

	@Autowired
	private DateUtils dateUtils;
	
	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	private final String ALL = "ALL";

	@RequestMapping(value = "/allproducts", method = RequestMethod.GET)
	public ModelAndView getBasicView(@ModelAttribute("orderExplorerForm") OrderExplorerForm orderExplorerForm) {
		//Set View
		ModelAndView mv = new ModelAndView();
		mv.setViewName("main");

		//Prepare Model Objects
		OrderStatistics orderStatistics = new OrderStatistics();
		
		orderStatistics.setPieChartUrl("/order-explorer/pie-chart.jpeg");
		orderStatistics.setXyChartUrl("/order-explorer/xy-chart.jpeg");
		orderStatistics.setTotalOrderCount(orderService.getTotalOrderCount());
		orderStatistics.setTotalSuccessCount(orderService.getSuccessfullyCompletedOrderCount());
		orderStatistics.setTotalRejectedCount(orderService.getRejectedOrderCount());
		orderStatistics.setTotalFailureCount(orderService.getSuccessfullyRequestedOrderCount());
		
		
		final String TODAY = DateType.TODAY.getType();
		Date today = dateUtils.getFromDate(TODAY, dateUtils.currentTime());
		Date now = dateUtils.now();
		
		orderStatistics.setCfaCount(orderService.getTotalOrderCountByType(OrderType.CFA.getName(), today, now));
		orderStatistics.setCfuCount(orderService.getTotalOrderCountByType(OrderType.CFU.getName(), today, now));
		orderStatistics.setAfaCount(orderService.getTotalOrderCountByType(OrderType.AFA.getName(), today, now));
		orderStatistics.setAfuCount(orderService.getTotalOrderCountByType(OrderType.AFU.getName(), today, now));

		//Add model objects
		mv.addObject("orderStats", orderStatistics);
		addModelAttributes(mv.getModel());
		
		return mv;
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public ModelAndView getProductView(@ModelAttribute("orderExplorerForm") OrderExplorerForm orderExplorerForm) {

		String orderType = orderExplorerForm.getOrderType();
		String make = orderExplorerForm.getMake();
		
		boolean bBasicView = false;
		if (!ALL.equals(orderType)) {
			if (!ALL.equals(make)) {
				bBasicView = true;
			} 
		} else {
			bBasicView = true;
		}
		
		if(bBasicView) {
			return getBasicView(orderExplorerForm);
		} else {
			String model = orderExplorerForm.getModel();
			
			String fromDateStr = orderExplorerForm.getFromDate();
			String toDateStr = orderExplorerForm.getToDate();
			String date = orderExplorerForm.getDate();
			
			Date fromDate = getDate(fromDateStr, date, true);
			Date toDate = getDate(toDateStr, date, false);
			
			
			OrderStatistics orderStatistics = new OrderStatistics();

			orderStatistics.setProduct(orderType);
			orderStatistics.setTotalOrderCount(orderService.getTotalOrderCountByType(orderType, fromDate, toDate));

			orderStatistics.getCustomCounts().put("OrdersRejected", orderService.getRejectedOrderCountByType(orderType, fromDate, toDate));
			orderStatistics.getCustomCounts().put("OrdersAccepted", orderService.getSuccessfullyCompletedOrderCountByType(orderType, fromDate, toDate));
			orderStatistics.getCustomCounts().put("OrdersSubmissionFailed", orderService.getSuccessfullyRequestedOrderCountByType(orderType, fromDate, toDate));

			orderStatistics.setAllBrands(orderService.getAllBrands());
			orderStatistics.setPieChartUrl("/order-explorer/pie-chart.jpeg?orderType=" + orderType + "&make=" + make + "&model=" + model 
					+ "&date=" + date + "&fromDate=" + fromDateStr + "&toDate=" + toDateStr);
			orderStatistics.setXyChartUrl("/order-explorer/xy-chart.jpeg?orderType=" + orderType + "&make=" + make + "&model=" + model 
					+ "&date=" + date + "&fromDate=" + fromDateStr + "&toDate=" + toDateStr);
			
			ModelAndView mv = new ModelAndView();
			mv.setViewName("product");
			
			mv.addObject("orderStats", orderStatistics);
			addModelAttributes(mv.getModel());
			
			return mv;
		}
	}
	
	@RequestMapping(value = "/pie-chart.jpeg", method = RequestMethod.GET)
	public void getPieChart(HttpServletRequest req, HttpServletResponse response, 
			@ModelAttribute("orderExplorerForm") OrderExplorerForm orderExplorerForm) throws IOException {
		response.setContentType("image/png");

		String orderType = orderExplorerForm.getOrderType();
		Date fromDate = getDate(orderExplorerForm.getFromDate(), orderExplorerForm.getDate(), true);
		Date toDate = getDate(orderExplorerForm.getToDate(), orderExplorerForm.getToDate(), false);

		long ordersAccepted = 0;
		long ordersRejected = 0;
		long ordersSubmissionFailed = 0;
		
		DefaultPieDataset dataSet = new DefaultPieDataset();
		if ((orderType != null) && (!"ALL".equals(orderType))) {
			ordersAccepted = orderService.getSuccessfullyCompletedOrderCountByType(orderType, fromDate, toDate);
			ordersRejected = orderService.getRejectedOrderCountByType(orderType, fromDate, toDate);
			ordersSubmissionFailed = orderService.getSuccessfullyRequestedOrderCountByType(orderType, fromDate, toDate);
		} else {
			ordersAccepted = orderService.getSuccessfullyCompletedOrderCount();
			ordersRejected = orderService.getRejectedOrderCount();
			ordersSubmissionFailed = orderService.getSuccessfullyRequestedOrderCount();
		}
		
		dataSet.setValue("OrderAccepted", ordersAccepted);
		dataSet.setValue("OrderRejected", ordersRejected);
		dataSet.setValue("SubmissionFailed", ordersSubmissionFailed);
		
		JFreeChart chart = ChartFactory.createPieChart("", dataSet, true, true, true);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("OrderAccepted", Color.GREEN);
		plot.setSectionPaint("OrderRejected", Color.RED);
		plot.setSectionPaint("SubmissionFailed", Color.YELLOW);
		plot.setBackgroundPaint(Color.WHITE);
		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 500, 400);
		response.getOutputStream().close();
	}

	@RequestMapping(value = "/xy-chart.jpeg", method = RequestMethod.GET)
	public void getLineGraph(HttpServletRequest req, HttpServletResponse response, 
			@ModelAttribute("orderExplorerForm") OrderExplorerForm orderExplorerForm) throws IOException {
		response.setContentType("image/png");
		XYSeries series1 = new XYSeries("Time in Hrs");
		series1.add(1.0, 1.0);
		series1.add(2.0, 2.0);
		series1.add(3.0, 3.0);
		series1.add(4.0, 5.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 3.0);
		series1.add(7.0, 2.0);
		series1.add(8.0, 1.0);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		JFreeChart chart = ChartFactory.createXYLineChart("", // Title
				"x-axis", // x-axis Label
				"y-axis", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.getPlot().setOutlineVisible(false);

		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 500, 400);
		response.getOutputStream().close();
	}
	
	/*
	 * Common Model attributes
	 */
	private void addModelAttributes(Map<String, Object> modelMap) {
		Map<String, List<String>> allBrandsMap = orderService.getAllBrands();
		
		modelMap.put("brands", getBrands(allBrandsMap));
		modelMap.put("models", getModels(allBrandsMap));
		modelMap.put("orderTypes", getOrderTypes());
		modelMap.put("dateTypes", getDateTypes());
	}
	
	private Date getDate(String dateStr, String dateType, boolean bFromDate) {
		Date retDate = null;
		if(StringUtils.isEmpty(dateStr)) {
			long currentTime = dateUtils.currentTime();
			retDate = bFromDate?dateUtils.getFromDate(dateType, currentTime):new Date(currentTime);
		} else {
			try {
				retDate = df.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return retDate;
	}
	
	private Map<String, String> getModels(Map<String, List<String>> allBrandsMap) {
		Map<String, String> modelsMap = new LinkedHashMap<String, String>();
		
		modelsMap.put("ALL", "ALL");
		for(String brand : allBrandsMap.keySet()) {
			for(String model : allBrandsMap.get(brand)) {
				modelsMap.put(model, model);
			}
		}

		return modelsMap;
	}
	
	private Map<String, String> getBrands(Map<String, List<String>> allBrandsMap) {
		Map<String, String> brandsMap = new LinkedHashMap<String, String>();
		
		brandsMap.put("ALL", "ALL");
		for(String brand : allBrandsMap.keySet()) {
			brandsMap.put(brand, brand);
		}

		return brandsMap;
	}
	
	private Map<String, String> getOrderTypes() {
		Map<String, String> ordreTypesMap = new LinkedHashMap<String, String>();
		
		for(OrderType type : OrderType.values()) {
			ordreTypesMap.put(type.getName(), type.getName());
		}

		return ordreTypesMap;
	}
	
	private Map<String, String> getDateTypes() {
		Map<String, String> dateTypesMap = new LinkedHashMap<String, String>();
		
		for(DateType type : DateType.values()) {
			dateTypesMap.put(type.getType(), type.getType());
		}

		return dateTypesMap;
	}
}
