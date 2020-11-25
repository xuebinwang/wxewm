package com.timeCommUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 说明：日期处理
 * @author wxb
 * @date 2020-11-25 11:04
 * @version
 */
public class DateUtil {

    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");

    private final static SimpleDateFormat sdfTimeMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
    public  static String getOrderNum(){
        Date date=new Date();
        return sdfTimes.format(date) + getThree();
    }

    /**
     * 产生随机的三位数
     * @return
     */
    public static String getThree(){
        Random rad=new Random();
        return rad.nextInt(1000)+"";
    }

    /**
     * 获取yyyyMMddHHmmss格式
     * @return
     */
    public static String getSdfTimes() {
        return sdfTimes.format(new Date());
    }

    /**
     * 获取YYYY格式
     * @return
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     * @return
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     * @return
     */
    public static String getDays(){
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm格式
     * @return
     */
    public static String getTimeMin() {
        return sdfTimeMin.format(new Date());
    }

    /**
     * 传入时间，获取YYYY-MM-DD HH:mm:ss格式
     * @return
     */
    public static String getTimeByDate(Date date) {
        return sdfTime.format(date);
    }

    /**
     * @Title: compareDate
     * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @author fh
     */
    public static boolean compareDate(String s, String e) {
        if(fomatDate(s)==null||fomatDate(e)==null){
            return false;
        }
        return fomatDate(s).getTime() >=fomatDate(e).getTime();
    }

    /**
     * 格式化日期:yyyy-MM-dd
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date fomatDateTime(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期:yyyy-MM-dd HH:mm
     * @return
     */
    public static Date fomatDateMin(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期:yyyyMMddHHmmss
     * @return
     */
    public static Date fomatDateTimes(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期:yyyy-MM
     * @return
     */
    public static Date fomatDateMonth(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验日期是否合法
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDiffYear(String startTime,String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //long aa=0;
            int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        //System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 获得指定日期的后n天，返回 yyyy-MM-dd HH:mm:ss
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayAfterTimes(Date specifiedDay,int days){
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day+days);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的前n天，返回 yyyy-MM-dd HH:mm:ss
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBeforeTimes(Date specifiedDay,int days){
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-days);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的前n天，返回yyyy-MM-dd
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(Date specifiedDay,int days){
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-days);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的前n分钟，返回 yyyy-MM-dd HH:mm:ss
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getTimesBeforeByMinutes(Date specifiedDay,int min){
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        long timeInMillis = c.getTimeInMillis();
        timeInMillis -= min * 60 * 1000;
        String times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeInMillis));

        return times;
    }

    /**
     * 获得指定日期的后n分钟，返回 yyyy-MM-dd HH:mm:ss
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getTimesAfterByMinutes(Date specifiedDay,int min){
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        long timeInMillis = c.getTimeInMillis();
        timeInMillis += min * 60 * 1000;
        String times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeInMillis));

        return times;
    }

    /**
     * 获取指定日期的今年日期
     * @return
     */
    public static Date SpecifiedDate(String date) {
        return fomatDate(getYear()+date.substring(4));
    }

    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }


    //获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 返回月份的日期集合
     * @param dateMonth
     * @return
     */
    public static List<String> dateList(String dateMonth) {
        Date month = fomatDate(dateMonth);//格式化日期
        List<String> dayList = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(month);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);

            dayList.add(df);
        }

        return dayList;
    }

    /**
     * 返回一周的日期集合
     * @param dateWeek
     * @return
     */
    public static List<String> dateListForWeek(String dateWeek) {
        Date week = fomatDate(dateWeek);//格式化日期
        List<String> dayList = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(week);//week为指定星期任意日期
        int dayNumOfWeek = 7;
        cal.set(Calendar.DAY_OF_WEEK, 1);// 从星期天开始

        for (int i = 0; i < dayNumOfWeek; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);

            dayList.add(df);
        }

        return dayList;
    }

    /**
     * 获取两个日期之间的日期
     * @param start 开始日期
     * @param end 结束日期
     * @return 日期集合
     */
    public static List<String> getBetweenDates(String start, String end) {
        List<String> result = new ArrayList<String>();
        try {
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(fomatDate(start));
            tempStart.add(Calendar.DAY_OF_YEAR, 0);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(fomatDate(end));
            tempEnd.add(Calendar.DAY_OF_YEAR, 1);
            while (tempStart.before(tempEnd)) {
                String day = new SimpleDateFormat("yyyy-MM-dd").format(tempStart.getTime());
                result.add(day);
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 获取两个日期之间的月份
     * @param start 开始日期
     * @param end 结束日期
     * @return 日期集合
     */
    public static List<String> getBetweenMonths(String start, String end) {
        List<String> result = new ArrayList<String>();
        try {
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(fomatDate(start));
            tempStart.add(Calendar.MONTH, 0);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(fomatDate(end));
            tempEnd.add(Calendar.DAY_OF_YEAR, 1);
            while (tempStart.before(tempEnd)) {
                String day = new SimpleDateFormat("yyyy-MM").format(tempStart.getTime());
                result.add(day);
                tempStart.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 获取两个日期之间的年份
     * @param start 开始日期
     * @param end 结束日期
     * @return 日期集合
     */
    public static List<String> getBetweenYears(String start, String end) {
        List<String> result = new ArrayList<String>();
        try {
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(fomatDate(start));
            tempStart.add(Calendar.YEAR, 0);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(fomatDate(end));
            tempEnd.add(Calendar.DAY_OF_YEAR, 1);
            while (tempStart.before(tempEnd)) {
                String day = new SimpleDateFormat("yyyy").format(tempStart.getTime());
                result.add(day);
                tempStart.add(Calendar.YEAR, 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 判断日期是星期几
     * @param pTime
     * @return
     * @throws Exception
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));

        int dayForWeek = 0;  //星期几
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }

        return dayForWeek;
    }

    public static void main(String[] args) {
        System.out.println(getTimesBeforeByMinutes(new Date(),30));
    }

}
