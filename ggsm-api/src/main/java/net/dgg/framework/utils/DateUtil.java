package net.dgg.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期Util
 *
 * @author hdc
 */
public class DateUtil {
    /**
     * 计算当期时间加上N天之后的日期
     *
     * @param day N天
     * @return N天后的日期
     * @throws ParseException 异常
     */
    public static Date dateFomartJs(int day) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String dd = fmt.format(date);
            Date df = fmt.parse(dd);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df);
            calendar.add(Calendar.DAY_OF_MONTH, day);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算当期时间加上N天之后的日期
     *
     * @param date 当期日期
     * @param day  N天
     * @return N天后的日期
     * @throws ParseException 异常
     */
    public static String dateFomartJs(Date date, int day) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String dd = fmt.format(date);
        Date df = fmt.parse(dd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        String T = fmt.format(calendar.getTime());
        return T;
    }

    /**
     * 计算传入参数时间加上N个月之后的日期
     *
     * @param date 当前时间
     * @param moth 月数
     * @return 结果
     */
    public static String dataAddMoth(Date date, int moth) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, moth);
        String T = fmt.format(calendar.getTime());
        return T;
    }

    /**
     * 计算当期时间减去N天之后的日期
     *
     * @param date 当期日期
     * @param day  N天
     * @return N天后的日期
     * @throws ParseException 异常
     */
    public static String dateDelDay(Date date, int day) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String dd = fmt.format(date);
        Date df = fmt.parse(dd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df);
        calendar.add(Calendar.DAY_OF_MONTH, 0 - day);
        String T = fmt.format(calendar.getTime());
        return T;
    }

    /**
     * 计算当期时间减去N小时之后的日期
     *
     * @param date 当期日期
     * @param hour N小时
     * @return N小时后的日期
     * @throws ParseException 异常
     */
    public static String dateDelHour(Date date, int hour) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dd = fmt.format(date);
        Date df = fmt.parse(dd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df);
        calendar.add(Calendar.HOUR_OF_DAY, 0 - hour);
        String T = fmt.format(calendar.getTime());
        return T;
    }


    /**
     * 计算当期时间加上N天之后的日期
     *
     * @param day N天
     * @return N小时后的日期
     * @throws ParseException 异常
     */
    public static Date dateAddDay(Integer day) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dd = fmt.format(new Date());
        Date df = fmt.parse(dd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df);
        calendar.add(Calendar.HOUR_OF_DAY, day * 24);
        String T = fmt.format(calendar.getTime());
        return stringToDate(T, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 计算当期时间加上N天之后的日期
     *
     * @param date 当期日期
     * @param day  N天
     * @return N天后的日期
     * @throws ParseException 异常
     */
    public static Date dateAddDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间的时间差
     *
     * @param smallDate 小的时间(yyyy-MM-dd HH:mm:ss)
     * @param bigDate   大的时间(yyyy-MM-dd HH:mm:ss)
     * @param type      时间差类型（分：min，时：hour，天：day）
     * @return
     */
    public static int timeBetween(Date smallDate, Date bigDate, String type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smallDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bigDate);
        long time2 = cal.getTimeInMillis();
        long between = time2 - time1;
        if (type.equals("day")) {
            between = between / (1000 * 60 * 60 * 24);
        } else if (type.equals("hour")) {
            between = between / (1000 * 60 * 60);
        } else if (type.equals("min")) {
            between = between / (1000 * 60);
        }
        return (int) between;
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetweenString(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前日期下周一的日期
     *
     * @return 下周一日期
     */
    public static String getNextMonday(Integer num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, num);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String monday = df.format(cal.getTime());
        return monday;
    }

    /**
     * 获取当前日期下周日的日期
     *
     * @return 下周日期
     */
    public static String getNextSunday(Integer num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, num + 1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String sunday = df.format(cal.getTime());
        return sunday;
    }

    /**
     * 日期转换为字符串
     *
     * @param date   日期
     * @param format 格式
     * @return
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return date == null ? "" : df.format(date);
    }

    /**
     * 日期转换为字符串
     * 默认：yyyy-MM-dd HH:mm:ss
     * @param date   日期
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date ,"yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 日期转换为Long
     *
     * @param date   日期
     * @param format 格式
     * @return
     */
    public static String dateToLong(Date date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long timeStart = sdf.parse("2011-09-20 12:30:45").getTime();
        System.out.println(timeStart);
        date = new Date(timeStart);
        return date == null ? "" : sdf.format(date);
    }

    /**
     * 字符串转换为日期
     *
     * @param dateStr 日期
     * @param format  格式
     * @return
     */
    public static Date stringToDate(String dateStr, String format) {
        Date date = new Date();
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当月第一天
     *
     * @return 当月第一天
     * @author tanghom<tanghom@qq.com>
     */
    public static Date monthFirstDay() {
        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取日期当月第一天
     *
     * @return 当月第一天
     */
    public static Date monthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当月最后一天
     *
     * @return 当月最后一天
     * @author tanghom<tanghom@qq.com>
     */
    public static Date monthLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取日期当月最后一天
     *
     * @return 当月最后一天
     * @author tanghom<tanghom@qq.com>
     */
    public static Date monthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取两个日期之间所有月份
     *
     * @param minDate 小日期
     * @param maxDate 大日期
     * @return list
     * @throws ParseException
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
        SimpleDateFormat resSdf = new SimpleDateFormat("yyyyMM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(resSdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    public static Date stringDateToDate(String dateStr){
        return stringDateToDate(dateStr,null);
    }

    /**
     * @param dateStr   日期字符串
     * @param formatStr 日期格式 （如果为空，默认根据yyyy-MM-dd HH:mm:ss 进行格式尝试）
     * @return 格式化成功 返回 Date 否则 为null
     */
    public static Date stringDateToDate(String dateStr, String formatStr) {

        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        if (StringUtils.isBlank(formatStr)) {

            try {
                return format1.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }

        } else {//指定了日期格式
            DateFormat format3 = new SimpleDateFormat(formatStr);
            try {
                return format3.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException("根据指定日期格式转换日期字符串为Date失败！", e);
            }
        }
    }

    public static String fromDateP(String pattern) {
        DateFormat format1 = new SimpleDateFormat(pattern);
        return format1.format(new Date());
    }

    public static String stringDateFormat(String str, String format) throws ParseException {
        Date date = stringToDate(str, "yyyy-MM-dd HH:mm:ss");
        String stringDate = dateToString(date, format);
        return stringDate;
    }

    public static Date long2Date(Long dateLong){
        Date date=new Date(dateLong);
        return date;
    }

}
