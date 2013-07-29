/**
 * 
 */
package co.uk.o2.orderexplorer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Phani Maddali
 * 
 */
public class OrderStatistics {

	private long totalOrderCount;

	private long totalSuccessCount;

	private long totalFailureCount;

	private long totalRejectedCount;

	private long cfuCount;

	private long cfaCount;

	private long afuCount;

	private long afaCount;

	private String product;

	private Map<String, List<String>> allBrands;

	private String make;

	private String model;

	private HashMap<String, Long> customCounts = new HashMap<String, Long>();

	private String pieChartUrl;

	private String xyChartUrl;

	public Set<String> getAllBrands() {
		return allBrands.keySet();
	}

	public List<String> getAllModelsForBrand(String brand) {
		return allBrands.get(brand);
	}

	/**
	 * @param allBrands
	 *            the allBrands to set
	 */
	public void setAllBrands(Map<String, List<String>> allBrands) {
		this.allBrands = allBrands;
	}

	/**
	 * @return the pieChartUrl
	 */
	public String getPieChartUrl() {
		return pieChartUrl;
	}

	/**
	 * @param pieChartUrl
	 *            the pieChartUrl to set
	 */
	public void setPieChartUrl(String pieChartUrl) {
		this.pieChartUrl = pieChartUrl;
	}

	/**
	 * @return the xyChartUrl
	 */
	public String getXyChartUrl() {
		return xyChartUrl;
	}

	/**
	 * @param xyChartUrl
	 *            the xyChartUrl to set
	 */
	public void setXyChartUrl(String xyChartUrl) {
		this.xyChartUrl = xyChartUrl;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make
	 *            the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the customCounts
	 */
	public HashMap<String, Long> getCustomCounts() {
		return customCounts;
	}

	/**
	 * @param customCounts
	 *            the customCounts to set
	 */
	public void setCustomCounts(HashMap<String, Long> customCounts) {
		this.customCounts = customCounts;
	}

	public long getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(long totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public long getTotalSuccessCount() {
		return totalSuccessCount;
	}

	public void setTotalSuccessCount(long totalSuccessCount) {
		this.totalSuccessCount = totalSuccessCount;
	}

	public long getTotalFailureCount() {
		return totalFailureCount;
	}

	public void setTotalFailureCount(long totalFailureCount) {
		this.totalFailureCount = totalFailureCount;
	}

	public long getTotalRejectedCount() {
		return totalRejectedCount;
	}

	public void setTotalRejectedCount(long totalRejectedCount) {
		this.totalRejectedCount = totalRejectedCount;
	}

	public long getCfuCount() {
		return cfuCount;
	}

	public void setCfuCount(long cfuCount) {
		this.cfuCount = cfuCount;
	}

	public long getCfaCount() {
		return cfaCount;
	}

	public void setCfaCount(long cfaCount) {
		this.cfaCount = cfaCount;
	}

	public long getAfuCount() {
		return afuCount;
	}

	public void setAfuCount(long afuCount) {
		this.afuCount = afuCount;
	}

	public long getAfaCount() {
		return afaCount;
	}

	public void setAfaCount(long afaCount) {
		this.afaCount = afaCount;
	}

}
