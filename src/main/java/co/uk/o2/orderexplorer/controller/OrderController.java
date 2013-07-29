/**
 * 
 */
package co.uk.o2.orderexplorer.controller;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import co.uk.o2.orderexplorer.model.OrderStatistics;
import co.uk.o2.orderexplorer.service.OrderService;

/**
 * @author Phani Maddali
 * 
 */

@Controller
@RequestMapping(value = "/order-explorer")
public class OrderController {

	@Autowired
	private OrderService orderService;

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@RequestMapping(value = "/allproducts", method = RequestMethod.GET)
	public ModelAndView getBasicView() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("main");

		OrderStatistics orderStatistics = new OrderStatistics();
		mv.addObject("orderStats", orderStatistics);
		orderStatistics.setPieChartUrl("/order-explorer/pie-chart.jpeg");
		orderStatistics.setXyChartUrl("/order-explorer/xy-chart.jpeg");
		orderStatistics.setTotalOrderCount(orderService.getTotalOrderCount());
		orderStatistics.setTotalSuccessCount(orderService
				.getSuccessfullyCompletedOrderCount());
		orderStatistics.setTotalRejectedCount(orderService
				.getRejectedOrderCount());
		orderStatistics.setTotalFailureCount(orderService
				.getSuccessfullyRequestedOrderCount());

		Date toDate = new Date(System.currentTimeMillis());

		orderStatistics.setCfaCount(orderService.getTotalOrderCountByType(
				"CFA", getFromDate("TODAY"), toDate));
		orderStatistics.setCfuCount(orderService.getTotalOrderCountByType(
				"CFU", getFromDate("TODAY"), toDate));
		orderStatistics.setAfaCount(orderService.getTotalOrderCountByType(
				"AFA", getFromDate("TODAY"), toDate));
		orderStatistics.setAfuCount(orderService.getTotalOrderCountByType(
				"AFU", getFromDate("TODAY"), toDate));
		orderStatistics.setAllBrands(orderService.getAllBrands());

		return mv;
	}

	private Date getFromDate(String type) {
		long time = System.currentTimeMillis();
		if ("1 WEEK".equals(type)) {
			time = time - 604800000;
		} else if ("1 MONTH".equals(type)) {
			time = time - 604800000 * 4;
		} else if ("3 MONTHS".equals(type)) {
			time = time - 604800000 * 4 * 3;
		}
		Calendar fromDate = new GregorianCalendar();
		fromDate.setTimeInMillis(time);
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		fromDate.set(Calendar.MILLISECOND, 0);		
		return fromDate.getTime();
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public ModelAndView getProductView(@RequestParam String product,
			@RequestParam String make, @RequestParam String model,
			@RequestParam String date, @RequestParam String toDate,
			@RequestParam String fromDate) {

		Date fromDateVal = null;
		Date toDateVal = null;
		if (fromDate == "" && toDate == "") {
			fromDateVal = getFromDate(date);
			toDateVal = new Date(System.currentTimeMillis());
		} else {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			try {
				fromDateVal = df.parse(fromDate);
				toDateVal = df.parse(toDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		if (!"ALL".equals(product)) {
			if (!"ALL".equals(make)) {
				return getBasicView();
			} else {
				ModelAndView mv = new ModelAndView();
				mv.setViewName("product");

				OrderStatistics orderStatistics = new OrderStatistics();
				mv.addObject("orderStats", orderStatistics);

				orderStatistics.setProduct(product);
				orderStatistics.setTotalOrderCount(orderService
						.getTotalOrderCountByType(product, fromDateVal,
								toDateVal));

				orderStatistics.getCustomCounts().put(
						"OrdersRejected",
						orderService.getRejectedOrderCountByType(product,
								fromDateVal, toDateVal));
				orderStatistics.getCustomCounts().put(
						"OrdersAccepted",
						orderService.getSuccessfullyCompletedOrderCountByType(
								product, fromDateVal, toDateVal));
				orderStatistics.getCustomCounts().put(
						"OrdersSubmissionFailed",
						orderService.getSuccessfullyRequestedOrderCountByType(
								product, fromDateVal, toDateVal));

				orderStatistics.setAllBrands(orderService.getAllBrands());
				orderStatistics
						.setPieChartUrl("/order-explorer/pie-chart.jpeg?product="
								+ product
								+ "&make="
								+ make
								+ "&model="
								+ model
								+ "&date="
								+ date
								+ "&fromDate="
								+ fromDate
								+ "&toDate=" + toDate);
				orderStatistics
						.setXyChartUrl("/order-explorer/xy-chart.jpeg?product="
								+ product + "&make=" + make + "&model=" + model
								+ "&date=" + date + "&fromDate=" + fromDate
								+ "&toDate=" + toDate);
				return mv;
			}
		} else {
			return getBasicView();
		}
	}

	@RequestMapping(value = "/pie-chart.jpeg", method = RequestMethod.GET)
	public void getPieChart(HttpServletRequest req,
			HttpServletResponse response,
			@RequestParam(required = false) String product,
			@RequestParam(required = false) String make,
			@RequestParam(required = false) String model,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String toDate,
			@RequestParam(required = false) String fromDate) throws IOException {
		response.setContentType("image/png");

		Date fromDateVal = null;
		Date toDateVal = null;
		if (fromDate == null && toDate == null) {
			fromDateVal = getFromDate(date);
			toDateVal = new Date(System.currentTimeMillis());
		} else {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			try {
				fromDateVal = df.parse(fromDate);
				toDateVal = df.parse(toDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		DefaultPieDataset dataSet = new DefaultPieDataset();
		if ((product != null) && (!"ALL".equals(product))) {
			dataSet.setValue("OrderAccepted", orderService
					.getSuccessfullyCompletedOrderCountByType(product,
							fromDateVal, toDateVal));
			dataSet.setValue("OrderRejected", orderService
					.getRejectedOrderCountByType(product, fromDateVal,
							toDateVal));
			dataSet.setValue("SubmissionFailed", orderService
					.getSuccessfullyRequestedOrderCountByType(product,
							fromDateVal, toDateVal));
		} else {
			dataSet.setValue("OrderAccepted",
					orderService.getSuccessfullyCompletedOrderCount());
			dataSet.setValue("OrderRejected",
					orderService.getRejectedOrderCount());
			dataSet.setValue("SubmissionFailed",
					orderService.getSuccessfullyRequestedOrderCount());
		}
		JFreeChart chart = ChartFactory.createPieChart("", dataSet, true, true,
				true);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("OrderAccepted", Color.GREEN);
		plot.setSectionPaint("OrderRejected", Color.RED);
		plot.setSectionPaint("SubmissionFailed", Color.YELLOW);
		plot.setBackgroundPaint(Color.WHITE);
		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 500,
				400);
		response.getOutputStream().close();
	}

	@RequestMapping(value = "/xy-chart.jpeg", method = RequestMethod.GET)
	public void getLineGraph(HttpServletRequest req,
			HttpServletResponse response,
			@RequestParam(required = false) String product,
			@RequestParam(required = false) String make,
			@RequestParam(required = false) String model,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String toDate,
			@RequestParam(required = false) String fromDate) throws IOException {
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

		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 500,
				400);
		response.getOutputStream().close();
	}
}
