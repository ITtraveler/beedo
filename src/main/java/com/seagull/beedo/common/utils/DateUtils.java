package com.seagull.beedo.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by hgs on 2017/3/28.
 */

public class DateUtils {
    public final static long ONE_DAY_SECONDS = 86400;
    public final static long ONE_DAY_MILL_SECONDS = 86400000;
    public static final String format_yMd = "yyyy-MM-dd";
    public static final String format_yMdHm = "yyyy-MM-dd HH:mm";
    public static final String format_yMdHms = "yyyy-MM-dd HH:mm:ss";
    public static final String format_Hm = "HH:mm:ss";
    public static final String format_MdHm = "MM-dd HH:mm";
    public static final String format_yyyy = "yyyy";
    public static final String format_MM = "MM";
    public static final String format_dd = "dd";

    private static Date date;

    public static Date getCurDate() {
        if (date == null) {
            date = new Date();
        }
        return date;
    }

    /**
     * lastDate到startDate 时间,
     *
     * @param startDate
     * @param lastDate
     * @return 为正说明在未来
     */
    public static int durationsDay(Date startDate, Date lastDate) {

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(lastDate);
        int lastDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(startDate);
        int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
        return lastDay - nowDay;
    }

    /**
     * 1：e > s ; 0:e = s ; -1: e < s;
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Deprecated
    public static int compareDate(Date startDate, Date endDate) {
        long time = endDate.getTime() - startDate.getTime();
        if (time > 0) {
            return 1;
        } else if (time < 0) {
            return -1;
        } else
            return 0;
    }

    /**
     * 得到dateFormat格式的String Date
     *
     * @param date
     * @return
     */
    public static String getStringDate(Date date, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }


    /**
     * 将指定格式的String日期转换为Date
     *
     * @param stringDate
     * @return
     */
    public static Date stringToDate(String stringDate, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将Date进行格式，得到格式化后的Date
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date getFormatDate(Date date, String dateFormat) {
        String stringDate = getStringDate(date, dateFormat);
        Date formatDate = stringToDate(stringDate, dateFormat);
        return formatDate;
    }

    public static String getStringTime(long time) {
        long day = time / (24 * 60 * 60 * 1000);
        Long hour = (time / (60 * 60 * 1000) - day * 24);
        Long min = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        Long s = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String sHour = String.valueOf(hour);
        String sMin = String.valueOf(min);
        if (sHour.length() == 1) {
            sHour = "0" + sHour;
        }
        if (sMin.length() == 1) {
            sMin = "0" + sMin;
        }
        return sHour + ":" + sMin;
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one
     * @param two
     * @return
     */
    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    /**
     * @param one
     * @param two
     * @return
     */
    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
    }

    /**
     * 取得两个日期的间隔天数
     *
     * @param one
     * @param two
     * @return
     */
    public static long getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 取得两个日期的间隔小时数
     *
     * @param one
     * @param two
     * @return
     */
    public static long getDiffHours(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 60 * 1000);
    }

    public static String getYear() {
        return getYear(new Date());
    }

    public static String getMonth() {
        return getMonth(new Date());
    }

    public static String getDay() {
        return getDay(new Date());
    }


    public static String getYear(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format_yyyy);
        return getDateString(date, dateFormat);
    }

    public static String getMonth(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format_MM);
        return getDateString(date, dateFormat);
    }

    public static String getDay(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format_dd);
        return getDateString(date, dateFormat);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }
        return dateFormat.format(date);
    }


    /**
     * 计算当前时间几小时之后的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 计算当前时间几分钟之后的时间
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     * @param date1
     * @param secs
     * @return
     */

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (secs * 1000));
    }

    /**
     * 取得新的日期
     *
     * @param date1 日期
     * @param days  天数
     * @return 新的日期
     */
    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }


    /**
     * 获取系统日期的前一天日期，返回Date
     *
     * @return
     */
    public static Date getBeforeDate() {
        Date date = new Date();

        return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS));
    }

    /**
     * 获取系统日期的前days天日期，返回Date
     *
     * @return
     */
    public static Date getBeforeDate(int days) {
        Date date = new Date();

        return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS * days));
    }

    /**
     * 获得指定时间当天起点时间
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(date);

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 获得一天的最后时刻
     */
    public static Date getDayEnd(Date date) {
        if (date == null)
            return null;
        return DateUtils.addSeconds(DateUtils.addDays(getDayBegin(date), 1), -1);
    }

    /**
     * 判断参date上min分钟后，是否小于当前时间
     *
     * @param date
     * @param min
     * @return
     */
    public static boolean dateLessThanNowAddMin(Date date, long min) {
        return addMinutes(date, min).before(new Date());

    }

    public static boolean isBeforeNow(Date date) {
        if (date == null)
            return false;
        return date.compareTo(new Date()) < 0;
    }

}
