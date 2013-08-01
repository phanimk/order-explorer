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
