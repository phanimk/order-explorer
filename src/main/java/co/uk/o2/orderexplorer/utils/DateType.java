package co.uk.o2.orderexplorer.utils;

import java.util.HashMap;
import java.util.Map;

public enum DateType {
	TODAY("TODAY"),
	ONEWEEK("1 WEEK"),
	ONEMONTH("1 MONTH"),
	THREEMONTHS("3 MONTHS"),
	CUSTOM("CUSTOM");
	
	private final String type;
	private final Map<String, DateType> dateTypeMap = new HashMap<String, DateType>();
	
	private DateType(String type) {
		this.type = type;
		dateTypeMap.put(type, this);
	}
	
	public String getType() {
		return type;
	}
	
	public DateType getDateType(String type) {
		return dateTypeMap.get(type);
	}
}
