/**
 * 
 */
package co.uk.o2.orderexplorer.controller;

import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import co.uk.o2.orderexplorer.model.OrderExploreOptions;
import co.uk.o2.orderexplorer.service.OrderService;

/**
 * @author Phani Maddali
 * 
 */

public class OrderControllerTest {

	private OrderController orderController;

	private OrderService orderService = mock(OrderService.class);

	@Before
	public void setUp() {
		orderController = new OrderController();
		orderController.setOrderService(orderService);
	}

	@Test
	public void testBasicView() {
		//OrderExploreOptions oeOp = new OrderExploreOptions();
		ModelAndView mv = orderController.getBasicView();
		Assert.assertEquals("main", mv.getViewName());
	}

}
