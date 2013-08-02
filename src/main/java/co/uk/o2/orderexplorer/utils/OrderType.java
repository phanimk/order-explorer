package co.uk.o2.orderexplorer.utils;

import java.util.HashMap;
import java.util.Map;

public enum OrderType {
	ALL("ALL"),
	CFU("CFU"),
	AFU("AFU"),
	CFA("CFA"),
	AFA("AFA");
	
	private final String name;
	private final Map<String, OrderType> orderTypeMap = new HashMap<String, OrderType>();
	
	private OrderType(String name) {
		this.name = name;
		orderTypeMap.put(name, this);
	}
	
	public String getName() {
		return name;
	}
	
	public OrderType getOrderType(String name) {
		return orderTypeMap.get(name);
	}
}
