package co.uk.o2.orderexplorer.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {
	public Date getFromDate(String type, long currentTime) {
		if (DateType.ONEWEEK.getType().equals(type)) {
			currentTime = currentTime - 604800000;
		} else if (DateType.ONEMONTH.getType().equals(type)) {
			currentTime = currentTime - 604800000 * 4;
		} else if (DateType.THREEMONTHS.getType().equals(type)) {
			currentTime = currentTime - 604800000 * 4 * 3;
		}
		Calendar fromDate = new GregorianCalendar();
		fromDate.setTimeInMillis(currentTime);
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		fromDate.set(Calendar.MILLISECOND, 0);
		return fromDate.getTime();
	}
	
	public Date now() {
		return new Date(currentTime());
	}
	
	public long currentTime() {
		return System.currentTimeMillis();
	}
}
