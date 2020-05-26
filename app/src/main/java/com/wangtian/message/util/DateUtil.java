package com.wangtian.message.util;

import java.util.Calendar;

public class DateUtil {

	/**
	 * 时间转换  
	 * @param date 格式是   2014-12-13 12:23:32
	 * @return 如果是今年就不带年份  2003年12月21日  12:40
	 */
	public static String getDate(String date){
		if(date != null){
			String[] strs = date.split("-");
			Calendar calendar = Calendar.getInstance();
			StringBuffer buffer = new StringBuffer();
//			int year  = calendar.get(Calendar.YEAR);
//			if(strs[0].equals(year+"")){
//			}else{
				buffer.append(strs[0]+"年");
//			}
			if(strs.length> 1){
				buffer.append(strs[1]+"月");
			}
			if(strs.length > 2){
				String[] str = strs[2].split(" ");
				buffer.append(str[0]+"日");
				String[] st = str[1].split(":");
				if(st.length >1){
					buffer.append("  "+st[0]+":"+st[1]);
				}
				
			}
		return buffer.toString();
		}else{
			return null;
		}
	}
	/**
	 * 时间转换  
	 * @param date 是毫秒值
	 * @return 如果是今年就不带年份  2003年12月21日  12:40
	 */
	public static String getDate1(String date){
		long tiem = Long.parseLong(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tiem);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		Calendar calendar2 = Calendar.getInstance();
		int year1 = calendar2.get(Calendar.YEAR);
		String min;
		if(minute < 10){
			min = "0"+minute;
		}else{
			min = minute+"";
		}
//		if(year == year1){
//			return month+"月"+day+"日"+"  "+hour+":"+min;
//		}
		
		if(month < 10 && (month+"").length() < 2){
			return year+"年"+"0"+month+"月"+day+"日"+"  "+hour+":"+min;
		}
		return year+"年"+month+"月"+day+"日"+"  "+hour+":"+min;
	}
//	/**
//	 * 时间转换  
//	 * @param date 格式是   2014-12-13 12:23:32
//	 * @return 如果是今年就不带年份  2003年12-21 12:40
//	 */
//	public static String getdate(String date){
//		if(date != null){
//			String[] strs = date.split("-");
//			Calendar calendar = Calendar.getInstance();
//			StringBuffer buffer = new StringBuffer();
//			int year  = calendar.get(Calendar.YEAR);
//			if(strs[0].equals(year+"")){
//			}else{
//				buffer.append(strs[0]+"年");
//			}
//			if(strs.length> 1){
//				buffer.append(strs[1]+"-");
//			}
//			if(strs.length > 2){
//				String[] str = strs[2].split(" ");
//				buffer.append(str[0]);
//				String[] st = str[1].split(":");
//				if(st.length >1){
//					buffer.append("  "+st[0]+":"+st[1]);
//				}
//				
//			}
//		return buffer.toString();
//		}else{
//			return null;
//		}
//	}
}
