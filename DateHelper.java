package com.financingplat.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateHelper {
    
    public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";  
    
    /**
     * 用来全局控制 上一周，本周，下一周的周数变化
     */
    private static int weeks = 0;
    /**
     * 一月最大天数
     */
    private static int MaxDate;
    /**
     * 一年最大天数
     */
    private static int MaxYear;

    /** 
     * 获得当前时间，格式yyyy-MM-dd hh:mm:ss 
     *  
     * @param
     * @return 
     */  
    public static String getCurrentDate() {  
        return getCurrentDate(C_TIME_PATTON_DEFAULT);  
    }  
  
    /** 
     * 获得当前时间，格式自定义 
     *  
     * @param format 
     * @return 
     */  
    public static String getCurrentDate(String format) {  
        if (!"".equals(format)) {  
            format = C_TIME_PATTON_DEFAULT;  
        }  
        Calendar day = Calendar.getInstance();  
        day.add(Calendar.DATE, 0);
        // "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(day.getTime());  
        return date;  
    }  
  
    /** 
     * 日期转字符串 
     *  
     * @param date 
     * @return 
     */  
    public static String date2String(Date date) {  
        SimpleDateFormat sdf = new SimpleDateFormat(C_TIME_PATTON_DEFAULT);  
        return sdf.format(date);  
    }  
  
    /** 
     * 获得昨天时间 
     *  
     * @return 
     */  
    public static String getYesterdayDate() {  
        return getYesterdayDate(C_TIME_PATTON_DEFAULT);  
    }  
  
    /** 
     * 获得昨天时间，格式自定义 
     *  
     * @param format 
     * @return 
     */  
    public static String getYesterdayDate(String format) {  
        if (!"".equals(format)) {  
            format = C_TIME_PATTON_DEFAULT;  
        }  
        Calendar day = Calendar.getInstance();  
        day.add(Calendar.DATE, -1);  
        SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"  
        String date = sdf.format(day.getTime());  
        return date;  
    }  
  
    /** 
     * @param startDay 需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
     * @param endDay 被比较的时间 为空(null)则为当前时间
     * @param stype 返回值类型 0为多少天，1为多少个月，2为多少年 
     * @return 举例： compareDate("2009-09-12", null, 0); //比较天 compareDate("2009-09-12", null, 1);//比较月 
     *         compareDate("2009-09-12", null, 2);//比较年 
     */  
    public static int compareDate(String startDay, String endDay, int stype) {  
        int n = 0;  
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";  
  
        endDay = endDay == null ? getCurrentDate("yyyy-MM-dd") : endDay;  
  
        DateFormat df = new SimpleDateFormat(formatStyle);  
        Calendar c1 = Calendar.getInstance();  
        Calendar c2 = Calendar.getInstance();  
        try {  
            c1.setTime(df.parse(startDay));  
            c2.setTime(df.parse(endDay));  
        } catch (Exception e3) {  
            System.out.println("wrong occured");  
        }  
        // List list = new ArrayList();
        // 循环对比，直到相等，n 就是所要的结果
        while (!c1.after(c2)) {
            // 这里可以把间隔的日期存到数组中 打印出来
            // list.add(df.format(c1.getTime()));
            n++;  
            if (stype == 1) {
                // 比较月份，月份+1
                c1.add(Calendar.MONTH, 1);
            } else {
                // 比较天数，日期+1
                c1.add(Calendar.DATE, 1);
            }  
        }  
        n = n - 1;  
        if (stype == 2) {  
            n = (int) n / 365;  
        }  
        // System.out.println(startDay+" -- "+endDay+" 相差多少"+u[stype]+":"+n);  
        return n;  
    }  
  
    /** 
     * 判断时间是否符合时间格式 
     */  
    public static boolean isDate(String date, String dateFormat) {  
        if (date != null) {  
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);  
            format.setLenient(false);  
            try {  
                format.format(format.parse(date));  
            } catch (ParseException e) {  
                return false;  
            }  
            return true;  
        }  
        return false;  
    }  
  
    /** 
     * 实现给定某日期，判断是星期几 date:必须yyyy-MM-dd格式 
     */  
    public static String getWeekday(String date) {  
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");  
        SimpleDateFormat sdw = new SimpleDateFormat("E");  
        Date d = null;  
        try {  
            d = sd.parse(date);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return sdw.format(d);  
    }  
  
    /** 
     * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp） 
     *  
     * @param dateString 需要转换为timestamp的字符串 
     * @return dataTime timestamp 
     */  
    public final static java.sql.Timestamp string2Time(String dateString) {  
        DateFormat dateFormat;
        // 设定格式
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        dateFormat.setLenient(false);  
        java.util.Date date = null;  
        try {  
            date = dateFormat.parse(dateString);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        // java.sql.Timestamp dateTime = new java.sql.Timestamp(date.getTime());
        // Timestamp类型,timeDate.getTime()返回一个long型
        return new java.sql.Timestamp(date.getTime());
    }  
  
    /** 
     * method 将字符串类型的日期转换为一个Date（java.sql.Date） 
     *  
     * @param dateString 需要转换为Date的字符串 
     * @return dataTime Date 
     */  
    public final static java.sql.Date string2Date(String dateString) {  
        DateFormat dateFormat;  
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);  
        dateFormat.setLenient(false);  
        java.util.Date date = null;  
        try {  
            date = dateFormat.parse(dateString);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        // java.sql.Date dateTime = new java.sql.Date(date.getTime());// sql类型  
        return new java.sql.Date(date.getTime());  
    }  
  
    /** 
     * 计算两个日期之间相差的天数 
     *  
     * @param date1 
     * @param date2 
     * @return 
     */  
    public static int daysBetween(Date date1, Date date2) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date1);  
        long time1 = cal.getTimeInMillis();  
        cal.setTime(date2);  
        long time2 = cal.getTimeInMillis();  
        long between_days = (time2 - time1) / (1000 * 3600 * 24);  
  
        return Integer.parseInt(String.valueOf(between_days));  
    }  
  
    /** 
     * 取得当前日期N天后的日期 
     *  
     * @param days
     * @return 
     */  
    public static Date addDays(int days) {  
        Calendar cal = Calendar.getInstance();  
  
        cal.add(Calendar.DAY_OF_MONTH, days);  
  
        return cal.getTime();  
    }  
  
    /** 
     * 取得指定日期N天后的日期 
     *  
     * @param date 
     * @param days 
     * @return 
     */  
    public static Date addXDays(Date date, int days) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
  
        cal.add(Calendar.DAY_OF_MONTH, days);  
  
        return cal.getTime();  
    }  
  
    //
    /**
     * @Description 记录考勤， 记录迟到、早退时间
     * @return java.lang.String
     **/
    public static String getState() {  
        String state = "正常";  
        DateFormat df = new SimpleDateFormat("HH:mm:ss");  
        Date d = new Date();  
        try {  
            Date d1 = df.parse("08:00:00");  
            Date d2 = df.parse(df.format(d));  
            Date d3 = df.parse("17:30:00");  
  
            int t1 = (int) d1.getTime();  
            int t2 = (int) d2.getTime();  
            int t3 = (int) d3.getTime();  
            if (t2 < t1) {
                // 除以1000是为了转换成秒
                long between = (t1 - t2) / 1000;
                long hour1 = between % (24 * 3600) / 3600;  
                long minute1 = between % 3600 / 60;  
  
                state = "迟到 ：" + hour1 + "时" + minute1 + "分";  
  
            } else if (t2 < t3) {
                // 除以1000是为了转换成秒
                long between = (t3 - t2) / 1000;
                long hour1 = between % (24 * 3600) / 3600;  
                long minute1 = between % 3600 / 60;  
                state = "早退 ：" + hour1 + "时" + minute1 + "分";  
            }  
            return state;  
        } catch (Exception e) {  
            return state;  
        }  
    }  
  
    /** 
     * 得到二个日期间的间隔天数 
     */  
    public static String getTwoDay(String sj1, String sj2) {  
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");  
        long day = 0;  
        try {  
            java.util.Date date = myFormatter.parse(sj1);  
            java.util.Date mydate = myFormatter.parse(sj2);  
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);  
        } catch (Exception e) {  
            return "";  
        }  
        return day + "";  
    }  
  
    /** 
     * 根据一个日期，返回是星期几的字符串 
     *  
     * @param sdate 
     * @return 
     */  
    public static String getWeek(String sdate) {  
        // 再转换为时间  
        Date date = strToDate(sdate);  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        // int hour=c.get(Calendar.DAY_OF_WEEK);  
        // hour中存的就是星期几了，其范围 1~7  
        // 1=星期日 7=星期六，其他类推  
        return new SimpleDateFormat("EEEE").format(c.getTime());  
    }  
  
    /** 
     * 将短时间格式字符串转换为时间 yyyy-MM-dd 
     *  
     * @param strDate 
     * @return 
     */  
    public static Date strToDate(String strDate) {  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        ParsePosition pos = new ParsePosition(0);  
        Date strtodate = formatter.parse(strDate, pos);  
        return strtodate;  
    }  
  
    /** 
     * 两个时间之间的天数 
     *  
     * @param date1 
     * @param date2 
     * @return 
     */  
    public static long getDays(String date1, String date2) {  
        if (date1 == null || "".equals(date1)) {
            return 0;
        }
        if (date2 == null || "".equals(date2)) {
            return 0;
        }
        // 转换为标准时间  
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");  
        java.util.Date date = null;  
        java.util.Date mydate = null;  
        try {  
            date = myFormatter.parse(date1);  
            mydate = myFormatter.parse(date2);  
        } catch (Exception e) {  
        }  
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);  
        return day;  
    }  
  
    /**
     * @Description 计算当月最后一天,返回字符串
     * @return java.lang.String
     **/
    public static String getDefaultDay() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 设为当前月的1号
        lastDate.set(Calendar.DATE, 1);
        // 加一个月，变为下月的1号
        lastDate.add(Calendar.MONTH, 1);
        // 减去一天，变为当月最后一天
        lastDate.add(Calendar.DATE, -1);
        str = sdf.format(lastDate.getTime());
        return str;  
    }  
  
    /**
     * @Description  上月第一天
     * @return java.lang.String
     **/
    public static String getPreviousMonthFirst() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 设为当前月的1号
        lastDate.set(Calendar.DATE, 1);
        // 减一个月，变为下月的1号
        lastDate.add(Calendar.MONTH, -1);
        //减去一天，变为当月最后一天
        // lastDate.add(Calendar.DATE,-1);
  
        str = sdf.format(lastDate.getTime());  
        return str;  
    }  
  
    /**
     * @Description 获取当月第一天
     * @Date 15:02 2018/8/21
     * @return java.lang.String
     **/
    public static String getFirstDayOfMonth() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 设为当前月的1号
        lastDate.set(Calendar.DATE, 1);
        str = sdf.format(lastDate.getTime());  
        return str;  
    }  
  
    /**
     * @Description 获得本周星期日的日期
     * @return java.lang.String
     **/
    public static String getCurrentWeekday() {  
        weeks = 0;  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);  
        Date monday = currentDate.getTime();  
  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    //
    /**
     * @Description 获取当天时间
     * @return java.lang.String
     **/
    public static String getNowTime(String dateformat) {  
        Date now = new Date();
        // 可以方便地修改日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        String hehe = dateFormat.format(now);  
        return hehe;  
    }  
  
    //
    /**
     * @Description 获得当前日期与本周日相差的天数
     * @return int
     **/
    public static int getMondayPlus() {  
        Calendar cd = Calendar.getInstance();  
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        // 因为按中国礼拜一作为第一天所以这里减1
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 1) {  
            return 0;  
        } else {  
            return 1 - dayOfWeek;  
        }  
    }  
  
    //
    /**
     * @Description 获得本周一的日期
     * @return java.lang.String
     **/
    public static String getMondayOFWeek() {  
        weeks = 0;  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus);  
        Date monday = currentDate.getTime();  
  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    /**
     * @Description 获得相应周的周六的日期
     * @return java.lang.String
     **/
    public static String getSaturday() {  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);  
        Date monday = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    /**
     * @Description 获得上周星期日的日期
     * @return java.lang.String
     **/
    public static String getPreviousWeekSunday() {  
        weeks = 0;  
        weeks--;  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);  
        Date monday = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    /**
     * @Description 获得上周星期一的日期
     * @return java.lang.String
     **/
    public static String getPreviousWeekday() {  
        weeks--;  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);  
        Date monday = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    /**
     * @Description 获得下周星期一的日期
     * @return java.lang.String
     **/
    public static String getNextMonday() {  
        weeks++;  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);  
        Date monday = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    /**
     * @Description 获得下周星期日的日期
     * @return java.lang.String
     **/
    public static String getNextSunday() {  
  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);  
        Date monday = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
  
    public static int getMonthPlus() {  
        Calendar cd = Calendar.getInstance();  
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        // 把日期设置为当月第一天
        cd.set(Calendar.DATE, 1);
        // 日期回滚一天，也就是最后一天
        cd.roll(Calendar.DATE, -1);
        MaxDate = cd.get(Calendar.DATE);  
        if (monthOfNumber == 1) {  
            return -MaxDate;  
        } else {  
            return 1 - monthOfNumber;  
        }  
    }  
  
    /**
     * @Description 获得上月最后一天的日期
     * @return java.lang.String
     **/
    public static String getPreviousMonthEnd() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 减一个月
        lastDate.add(Calendar.MONTH, -1);
        // 把日期设置为当月第一天
        lastDate.set(Calendar.DATE, 1);
        // 日期回滚一天，也就是本月最后一天 
        lastDate.roll(Calendar.DATE, -1);
        str = sdf.format(lastDate.getTime());  
        return str;  
    }  
  
    /**
     * @Description 获得下个月第一天的日期
     * @return java.lang.String
     **/ 
    public static String getNextMonthFirst() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 减一个月 
        lastDate.add(Calendar.MONTH, 1);
        // 把日期设置为当月第一天 
        lastDate.set(Calendar.DATE, 1); 
        str = sdf.format(lastDate.getTime());  
        return str;  
    }  
  
    /**
     * @Description 获得下个月最后一天的日期
     * @return java.lang.String
     **/  
    public static String getNextMonthEnd() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 加一个月
        lastDate.add(Calendar.MONTH, 1);
        // 把日期设置为当月第一天
        lastDate.set(Calendar.DATE, 1);
        // 日期回滚一天，也就是本月最后一天
        lastDate.roll(Calendar.DATE, -1);
        str = sdf.format(lastDate.getTime());  
        return str;  
    }  
  
    /**
     * @Description 获得明年最后一天的日期
     * @return java.lang.String
     **/
    public static String getNextYearEnd() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 加一个年
        lastDate.add(Calendar.YEAR, 1);
        lastDate.set(Calendar.DAY_OF_YEAR, 1);  
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);  
        str = sdf.format(lastDate.getTime());  
        return str;  
    }  
  
    /**
     * @Description 获得明年第一天的日期
     * @return java.lang.String
     **/
    public static String getNextYearFirst() {  
        String str = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
        Calendar lastDate = Calendar.getInstance();
        // 加一个年
        lastDate.add(Calendar.YEAR, 1);
        lastDate.set(Calendar.DAY_OF_YEAR, 1);  
        str = sdf.format(lastDate.getTime());  
        return str;  
  
    }  
  
    /**
     * @Description 获得本年有多少天
     * @return int
     **/
    public static int getMaxYear() {  
        Calendar cd = Calendar.getInstance();
        // 把日期设为当年第一天
        cd.set(Calendar.DAY_OF_YEAR, 1);
        // 把日期回滚一天。
        cd.roll(Calendar.DAY_OF_YEAR, -1);
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);  
        return MaxYear;  
    }  
  
    public static int getYearPlus() {  
        Calendar cd = Calendar.getInstance();
        // 获得当天是一年中的第几天
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);
        // 把日期设为当年第一天
        cd.set(Calendar.DAY_OF_YEAR, 1);
        // 把日期回滚一天。
        cd.roll(Calendar.DAY_OF_YEAR, -1);
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);  
        if (yearOfNumber == 1) {  
            return -MaxYear;  
        } else {  
            return 1 - yearOfNumber;  
        }  
    }  
  
    /**
     * @Description 获得本年第一天的日期
     * @return java.lang.String
     **/
    public static String getCurrentYearFirst() {  
        int yearPlus = getYearPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, yearPlus);  
        Date yearDay = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preYearDay = df.format(yearDay);  
        return preYearDay;  
    }  
  
    /**
     * @Description 获得本年最后一天的日期
     * @return java.lang.String
     **/
    public static String getCurrentYearEnd() {  
        Date date = new Date();
        // 可以方便地修改日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String years = dateFormat.format(date);  
        return years + "-12-31";  
    }  
  
    /**
     * @Description 获得上年第一天的日期
     * @return java.lang.String
     **/
    public static String getPreviousYearFirst() {  
        Date date = new Date();
        // 可以方便地修改日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String years = dateFormat.format(date);  
        int years_value = Integer.parseInt(years);  
        years_value--;  
        return years_value + "-1-1";  
    }  
  
    /**
     * @Description 获得上年最后一天的日期
     * @return java.lang.String
     **/
    public static String getPreviousYearEnd() {  
        weeks--;  
        int yearPlus = getYearPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));  
        Date yearDay = currentDate.getTime();  
        DateFormat df = DateFormat.getDateInstance();  
        String preYearDay = df.format(yearDay);  
        getThisSeasonTime(11);  
        return preYearDay;  
    }  
  
    /**
     * @Description 获得本季度
     * @return java.lang.String
     **/
    public static String getThisSeasonTime(int month) {
        int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;  
        if (month >= 1 && month <= 3) {  
            season = 1;  
        }  
        if (month >= 4 && month <= 6) {  
            season = 2;  
        }  
        if (month >= 7 && month <= 9) {  
            season = 3;  
        }  
        if (month >= 10 && month <= 12) {  
            season = 4;  
        }  
        int start_month = array[season - 1][0];  
        int end_month = array[season - 1][2];  
  
        Date date = new Date();
        // 可以方便地修改日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String years = dateFormat.format(date);  
        int years_value = Integer.parseInt(years);
        // years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int start_days = 1;
        int end_days = getLastDayOfMonth(years_value, end_month);  
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;  
        return seasonDate;  
  
    }  
  
    /** 
     * 获取某年某月的最后一天 
     *  
     * @param year 年 
     * @param month 月 
     * @return 最后一天 
     */  
    public static int getLastDayOfMonth(int year, int month) {  
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {  
            return 31;  
        }  
        if (month == 4 || month == 6 || month == 9 || month == 11) {  
            return 30;  
        }  
        if (month == 2) {  
            if (isLeapYear(year)) {  
                return 29;  
            } else {  
                return 28;  
            }  
        }  
        return 0;  
    }  
  
    /** 
     * 是否闰年 
     *  
     * @param year 年 
     * @return 
     */  
    public static boolean isLeapYear(int year) {  
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);  
    }  
    
    /**
     * 根据时间获取这是一年中的第多少周
     * @param date
     * @return
     */
    public static int getYearOfWeekByDate(String date){  
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar cal = Calendar.getInstance(); 
        cal.clear();
        try {
            cal.setTime(fmt.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取周期数据
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;  
    }  
    
    
    /**
     * @Description 获取系统时间
     * @return java.lang.String
     **/
    public static String getSystemDay() {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar lastDate = Calendar.getInstance();  
        return sdf.format(lastDate.getTime());  
    }  
    
    
    
    /** 
     * 得到当前日期是周几，返回的是数字 0是周日--6是周六 
     * @param date 
     * @return 
     */  
    public static int getThisDateOfWeek(String date){  
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8);
        Calendar cal = Calendar.getInstance(); 
        cal.clear();
//        cal.set(Calendar.YEAR, new Integer(year));
//        cal.set(Calendar.MONTH, new Integer(month));
//        cal.set(Calendar.DAY_OF_YEAR, 1);
            try {
                cal.setTime(fmt.parse(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//        cal.add(Calendar.DAY_OF_YEAR, -1);
        // 获得今天是一周的第几天，星期日是第一天，星期一是第二天......
        // 因为按中国礼拜一作为第一天所以这里减1
        int dayOfWeek = cal.get(Calendar.WEEK_OF_YEAR);
        return dayOfWeek;  
    }  
    
    
    
    public static void main(String[] args){
        System.out.println(DateHelper.getYearOfWeekByDate("2017-06-20"));
        System.out.println(DateHelper.getSystemDay());
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-25"));
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-26"));
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-27"));
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-28"));
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-29"));
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-30"));
//        System.out.println(DateHelper.getThisDateOfWeek("2016-12-31"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-01"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-02"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-03"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-04"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-05"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-06"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-07"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-01-08"));
//        
//        System.out.println(DateHelper.getThisDateOfWeek("2017-02-27"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-31"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-30"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-29"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-28"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-27"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-26"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-25"));
//        System.out.println(DateHelper.getThisDateOfWeek("2017-12-24"));
        
        
        
    }
    
   
}
