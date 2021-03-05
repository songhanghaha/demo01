package com.example.storage.modules.wxuser.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimesUtils {
  
	public static String formatyyyyMMddHHmmssSSS="yyyy-MM-dd HH:mm:ss.SSS";
	public static String DateFormtime(String fromat) {
		SimpleDateFormat sdf=new SimpleDateFormat(fromat);
		return sdf.format(new Date());
	}
}
