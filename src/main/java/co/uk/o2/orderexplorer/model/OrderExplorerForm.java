package co.uk.o2.orderexplorer.model;


public class OrderExplorerForm {
	private String orderType;
	private String make;
	private String model;
	private String date;
	private String toDate;
	private String fromDate;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		if(obj != null && obj instanceof OrderExplorerForm) {
			retVal = this.toString().equals(((OrderExplorerForm)obj).toString());
		}
		
		return retVal;
	}
	
	@Override
	public String toString() {
		return 
		"[orderType="+orderType+
		",make="+make+
		",model="+model+
		",date="+date+
		",toDate="+toDate+
		",fromDate="+fromDate+"]";
	}
}
