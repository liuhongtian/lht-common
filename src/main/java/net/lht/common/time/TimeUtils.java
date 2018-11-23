package net.lht.common.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

	private static final DateTimeFormatter GMT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
	private static final String GMT = " GMT";

	/**
	 * 格式化当前时间到GMTString（格林威治标准时间），格式为：<br>
	 * dd MMM yyyy HH:mm:ss GMT，例如：<br>
	 * 19 Dec 2017 02:33:12 GMT
	 * 
	 * @return
	 */
	public static String nowToGMTString() {
		return LocalDateTime.now(ZoneId.of("GMT")).format(GMT_FORMATTER) + GMT;
	}
	
	public static void main(String[] args) {
		System.out.println(nowToGMTString());
	}	

}
