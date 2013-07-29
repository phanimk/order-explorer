/**
 * 
 */
package co.uk.o2.orderexplorer.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	long getTotalOrderCountByType(String type, Date fromDate, Date toDate);

	long getSuccessfullyRequestedOrderCountByType(String type, Date fromDate,
			Date toDate);

	long getSuccessfullyCompletedOrderCountByType(String type, Date fromDate,
			Date toDate);

	long getRejectedOrderCountByType(String type, Date fromDate, Date toDate);

	/* Statistics based on make */

	long getTotalOrderCountByMake(String make);

	long getSuccessfullyRequestedOrderCountByMake(String make);

	long getSuccessfullyCompletedOrderCountByMake(String make);

	long getRejectedOrderCountByMake(String make);

	long getSuccessfullyRequestedOrderCount(String type, String brand,
			String model, Date fromDate, Date toDate);

	long getSuccessfullyCompletedOrderCount(String type, String brand,
			String model, Date fromDate, Date toDate);

	long getRejectedOrderCount(String type, String brand, String model,
			Date fromDate, Date toDate);

	long getTotalOrderCount(String type, String brand, String model,
			Date fromDate, Date toDate);

	Map<String, List<String>> getAllBrands();

	List<String> getAllModelsForBrand(String brand);

}
