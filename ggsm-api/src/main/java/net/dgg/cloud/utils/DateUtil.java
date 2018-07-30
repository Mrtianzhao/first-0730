package net.dgg.cloud.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public final static String  TIME = "yyyyMMdd HH:mm";
	public final static String DATE = "yyyyMMdd";
	public final static String HOUR = "HH:mm";
	
	public static String parseString(Date date,String pattern){
		SimpleDateFormat simple = new SimpleDateFormat(pattern);
		return simple.format(date);
	}
}
