package com.template.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TimeUtil {
	private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);


	public static String getNowYear() {
		Calendar cal = Calendar.getInstance(); // 현재시간 setting
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date now_date = cal.getTime();

		return dateFormat.format(now_date);
	}

	public static String getNowDateToString() {
		Calendar cal = Calendar.getInstance(); // 현재시간 setting
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now_date = cal.getTime();

		return dateFormat.format(now_date);
	}

	public static Date getNowDate() {
		Calendar cal = Calendar.getInstance(); // 현재시간 setting
		Date now_date = cal.getTime();

		return now_date;
	}

	public static String getNowDateToString(String pattern) {
		Calendar cal = Calendar.getInstance(); // 현재시간 setting
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		Date now_date = cal.getTime();

		return dateFormat.format(now_date);
	}

	public static String getNowDateString() {
		Calendar cal = Calendar.getInstance();
		TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");
		cal.setTimeZone(zone);
		long now_time = cal.getTimeInMillis();
		Date date = new Date(now_time);
		String str_nowTime = (new SimpleDateFormat("yyyy-MM-dd").format(date));

		return str_nowTime;
	}

}
