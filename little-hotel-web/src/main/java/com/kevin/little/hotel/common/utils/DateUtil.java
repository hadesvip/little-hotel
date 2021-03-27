package com.kevin.little.hotel.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DateUtils;

/**
 * 日期工具类
 *
 * @author wangyong
 */
public final class DateUtil {

  /**
   * 日期格式
   */
  public enum DateFormatEnum {

    FULL_TIME_PATTERN("yyyyMMddHHmmss"),

    Y_M_D_PATTERN("yyyyMMdd"),

    FULL_TIME_SPLIT_PATTERN("yyyy-MM-dd HH:mm:ss"),

    CST_TIME_PATTERN("EEE MMM dd HH:mm:ss zzz yyyy");

    DateFormatEnum(java.lang.String pattern) {
      this.pattern = pattern;
    }

    /**
     * 日期格式
     */
    private final String pattern;

    public String getPattern() {
      return pattern;
    }

  }


  /**
   * 私有化一波
   */
  private DateUtil() {
  }

  public static String formatFullTime(LocalDateTime localDateTime) {
    return formatFullTime(localDateTime, DateFormatEnum.FULL_TIME_PATTERN.pattern);
  }

  public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    return localDateTime.format(dateTimeFormatter);
  }

  public static String getDateFormat(Date date, String dateFormatType) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
    return simpleDateFormat.format(date);
  }

  public static String formatCstTime(String date, String format) throws ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        DateFormatEnum.CST_TIME_PATTERN.pattern, Locale.US);
    Date usDate = simpleDateFormat.parse(date);
    return DateUtil.getDateFormat(usDate, format);
  }

  public static String formatInstant(Instant instant, String format) {
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    return localDateTime.format(DateTimeFormatter.ofPattern(format));
  }

  public static String format(Date date, String pattern) {
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    return format.format(date);
  }

  public static Date addTime(Date date, Long time, TimeUnit timeUnit) {
    return DateUtils.addSeconds(date, (int) timeUnit.toSeconds(time));
  }


}
