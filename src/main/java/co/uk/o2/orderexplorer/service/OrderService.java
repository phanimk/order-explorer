/**
 * 
 */
package co.uk.o2.orderexplorer.service;

/**
 * @author Phani Maddali
 * 
 */
public interface OrderService {

	long getTotalOrderCount();

	long getSuccessfullyRequestedOrderCount();

	long getSuccessfullyCompletedOrderCount();

	long getRejectedOrderCount();

	/* Statistics based on type */

	long getTotalOrderCountByType(String type);

	long getSuccessfullyRequestedOrderCountByType(String type);

	long getSuccessfullyCompletedOrderCountByType(String type);

	long getRejectedOrderCountByType(String type);

	/* Statistics based on make */

	long getTotalOrderCountByMake(String make);

	long getSuccessfullyRequestedOrderCountByMake(String make);

	long getSuccessfullyCompletedOrderCountByMake(String make);

	long getRejectedOrderCountByMake(String make);

}
