package com.common.utils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by  2016/4/19.
 */
public class DateFormatUtils {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_PATTERN_MINI = "yyyy-MM-dd HH:mm";
    public static final String DATETIME = "yyyy-MM-dd";
    public static final String YEAR = "yyyy";

    /**
     * 日期格式化：yyyy-MM-dd HH:mm:ss
     */
    public static String dateLFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_PATTERN);
        String str = format.format(date);
        return str;
    }
    public static String dateLFormatForMini(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_PATTERN_MINI);
        String str = format.format(date);
        return str;
    }
    public static String getDay(Date date) {
        if( null == date ){
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(DATETIME);
        String str = format.format(date);
        return str;
    }
    public static int getYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(YEAR);
        String str = format.format(date);
        return new Integer( str );
    }

    /**
     * 日期格式化：yyyy-MM-dd HH:mm:ss
     */
    public static Date dateStrFormat(String date) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_PATTERN);
        Date da = null;
		try {
			da = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return da;
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getMouthDay(){
        Calendar cal = Calendar.getInstance();
        int mouth = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        String mouthDay = mouth + (day + "");
        return new Integer( mouthDay );
    }
    public static int getDay(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }





        public static void main(String[] args) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            int dom = cal.get(Calendar.DAY_OF_MONTH);
            int doy = cal.get(Calendar.DAY_OF_YEAR);

            System.out.println("Current Date: " + cal.getTime());
            System.out.println("Day: " + day);
            System.out.println("Month: " + month);
            System.out.println("Year: " + year);
            System.out.println("Day of Week: " + dow);
            System.out.println("Day of Month: " + dom);
            System.out.println("Day of Year: " + doy);
        }

}
