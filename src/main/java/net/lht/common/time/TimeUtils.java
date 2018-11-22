package net.lht.common.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class TimeUtils {

	public static final String FULL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss ww,e";
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String getFullTimeString() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FULL_TIME_FORMAT);
		String dtStr = formatter.format(ldt);
		return dtStr;
	}

	public static String getTimeString() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
		String dtStr = formatter.format(ldt);
		return dtStr;
	}

	public static String getFullTimeString(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FULL_TIME_FORMAT);
		String dtStr = formatter.format(time);
		return dtStr;
	}

	public static String getTimeString(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
		String dtStr = formatter.format(time);
		return dtStr;
	}

	public static String getTimeString(LocalDateTime time, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		String dtStr = formatter.format(time);
		return dtStr;
	}

	public static long getLocalEpochSecond() {
		int offsetHours = TimeZone.getDefault().getRawOffset() / 3600000;
		Long time = Long.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(offsetHours)));
		return time;
	}

	public static void main(String[] args) {
		System.out.println(getTimeString());
	}

}
