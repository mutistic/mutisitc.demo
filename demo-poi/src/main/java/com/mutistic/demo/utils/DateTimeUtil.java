package com.mutistic.demo.utils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期 工具类
 *
 * @author mutistic
 */
public final class DateTimeUtil {

  private DateTimeUtil() {
  }

  private static Map<DatePatternEnum, String> DATE_PATTER_MAP = null;

  static {
    DATE_PATTER_MAP = new HashMap<>(DatePatternEnum.values().length);
    for (DatePatternEnum value : DatePatternEnum.values()) {
      DATE_PATTER_MAP.put(value, value.matche);
    }
  }

  /**
   * 默认格式器：yyyy-MM-dd
   */
  public final static DateTimeFormatter D_FORMATTER_DEFAULT = DateTimeFormatter
      .ofPattern(DatePatternEnum.YYYY_MM_DD.getPattern());
  /**
   * 默认格式器：yyyy-MM-dd HH:mm:ss
   */
  public final static DateTimeFormatter DT_FORMATTER_DEFAULT = DateTimeFormatter
      .ofPattern(DatePatternEnum.DEFAULT.getPattern());
  /**
   * 北京时区：+8
   */
  public final static ZoneOffset ZONE_OFFSET_BEIJING = ZoneOffset.of("+8");
  /**
   * 日期类型：UTC
   */
  public final static String UTC = "UTC";
  /**
   * 日期类型：CST
   */
  public final static String CST = "CST";

  public static DatePatternEnum getPatter(String dateStr) {
    if (StringUtils.isBlank(dateStr)) {
      return null;
    }
    for (Entry<DatePatternEnum, String> entry : DATE_PATTER_MAP.entrySet()) {
      if (dateStr.matches(entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }

  public static boolean isDateEnum(DatePatternEnum pattern) {
    return pattern == DatePatternEnum.YYYY_MM_DD ||
        pattern == DatePatternEnum.YYYY_MM_DD_2 ||
        pattern == DatePatternEnum.YYYYMMDD;
  }

  /**
   * 解析成日期字符串
   *
   * @param date 日期
   * @return 字符串：默认 yyyy-MM-dd HH:mm:ss 格式
   */
  public static String format(LocalDateTime date) {
    if (date == null) {
      return null;
    }
    return DT_FORMATTER_DEFAULT.format(date);
  }

  /**
   * 解析成默认格式日期字符串
   *
   * @param dateStr 日期字符串（未知类型）
   * @return 默认格式日期字符串
   */
  public static String specialFormat(String dateStr) {
    if (StringUtils.isBlank(dateStr)) {
      return dateStr;
    }
    //
    boolean isUTC = dateStr.contains("UTC");
    if (isUTC) {
      dateStr = dateStr.replace(" UTC", "");
    }
    DatePatternEnum patternEnum = getPatter(dateStr);
    if (patternEnum == null || patternEnum == DatePatternEnum.DEFAULT) {
      return dateStr;
    }
    System.out.println(patternEnum.pattern);
    DateTimeFormatter dateTimeFormatter = !dateStr.contains("CST") ?
        DateTimeFormatter.ofPattern(patternEnum.pattern) :
        DateTimeFormatter.ofPattern(patternEnum.pattern, Locale.US);
    // 只有日期
    LocalDateTime dateTime = null;
    if (isDateEnum(patternEnum)) {
      LocalDate date = dParse(dateStr, dateTimeFormatter);
      dateTime = LocalDateTime.of(date, LocalTime.MIN);
    } else {
      dateTime = dtParse(dateStr, dateTimeFormatter);
    }
    if (isUTC) {
      dateTime = dateTime.plusHours(8);
    }
    return format(dateTime);
  }

  /**
   * 解析成日期字符串
   *
   * @param date   日期
   * @param format 格式器
   * @return 日期字符串
   */
  public static String format(LocalDateTime date, DateTimeFormatter format) {
    if (date == null || format == null) {
      return null;
    }
    return format.format(date);
  }

  /**
   * 解析成日期+时间
   *
   * @param dateStr 日期字符串：默认 yyyy-MM-dd HH:mm:ss
   * @return LocalDateTime
   */
  public static LocalDateTime dtParse(String dateStr) {
    if (StringUtils.isBlank(dateStr)) {
      return null;
    }
    return LocalDateTime.parse(dateStr, DT_FORMATTER_DEFAULT);
  }

  /**
   * 解析成日期
   *
   * @param dateStr 日期字符串
   * @param format  格式器
   * @return 日期
   */
  public static LocalDate dParse(String dateStr, DateTimeFormatter format) {
    if (StringUtils.isBlank(dateStr) || format == null) {
      return null;
    }
    return LocalDate.parse(dateStr, format);
  }

  /**
   * 解析成日期
   *
   * @param dateStr 日期字符串：默认 yyyy-MM-dd HH:mm:ss
   * @return LocalDateTime
   */
  public static LocalDate dParse(String dateStr) {
    if (StringUtils.isBlank(dateStr)) {
      return null;
    }
    return LocalDate.parse(dateStr, D_FORMATTER_DEFAULT);
  }

  /**
   * 解析成日期+时间
   *
   * @param dateStr 日期字符串
   * @param format  格式器
   * @return 日期
   */
  public static LocalDateTime dtParse(String dateStr, DateTimeFormatter format) {
    if (StringUtils.isBlank(dateStr) || format == null) {
      return null;
    }
    return LocalDateTime.parse(dateStr, format);
  }

  /**
   * 解析成日期+时间
   *
   * @param millis 毫秒数
   * @return 日期
   */
  public static LocalDateTime dtParse(long millis) {
    if (millis <= 0) {
      return null;
    }
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZONE_OFFSET_BEIJING);
  }

  /**
   * 格式化为毫秒数
   *
   * @param date 日期
   * @return 毫秒数
   */
  public static long toMillis(LocalDateTime date) {
    if (date == null) {
      return 0;
    }
    return date.toInstant(ZONE_OFFSET_BEIJING).toEpochMilli();
  }

  public static void main(String[] args) throws ParseException {
    System.out.println(specialFormat("2019/01/02"));
    System.out.println(specialFormat("2019-01-02 11:11:11"));
    System.out.println(specialFormat("Mon Dec 02 18:00:00 CST 2019"));
//    System.out.println(specialFormat("Mon Dec 02 CST 2019"));

//    String dateStr = "2019/1/2  11:00:12 UTC";
//    System.out.println(stringToDate(dateStr));

//    String str = "Mon Dec 02 10:00:00 CST 2019";
//    String a = "EEE MMM dd HH:mm:ss z yyyy";
//    System.out.println(format(LocalDateTime.parse(str, DateTimeFormatter.ofPattern(a, Locale.US))));
  }

  /**
   * 日期格式化规则枚举
   */
  public enum DatePatternEnum {
    /**
     * yyyy-MM-dd
     */
    YYYY_MM_DD("yyyy-MM-dd", "^[0-9]{4}-\\d{2}-\\d{2}"),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    DEFAULT("yyyy-MM-dd HH:mm:ss", "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"),
    /**
     * yyyy-MM-dd HH:mm:ss.ssss
     */
    YYYY_MM_DD_HH_MM_SS_SSSS("yyyy-MM-dd HH:mm:ss.ssss",
        "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,4}$"),
    /**
     * yyyy/MM/dd
     */
    YYYY_MM_DD_2("yyyy/MM/dd", "^\\d{4}/\\d{2}/\\d{2}"),
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS_2("yyyy/MM/dd HH:mm:ss", "^\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}$"),
    /**
     * yyyy/MM/dd HH:mm:ss.ssss
     */
    YYYY_MM_DD_HH_MM_SS_SSSS_2("yyyy/MM/dd HH:mm:ss.sss",
        "^\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,4}$"),
    /**
     * yyyyMMdd
     */
    YYYYMMDD("yyyyMMdd", "^\\d{8}$"),
    /**
     * yyyyMMddHHmmss
     */
    YYYYMMDDHHMMSS("yyyyMMddHHmmss", "^\\d{15}$"),
    /**
     * yyyyMMddHHmmssssss
     */
    YYYYMMDDHHMMSSSSSS("yyyyMMddHHmmssssss", "^\\d{15}\\d{1,4}$"),
    /**
     * CST：EEE MMM dd HH:mm:ss z yyyy > Mon Dec 02 10:00:00 CST 2019
     */
    CST_YMDHMS("EEE MMM dd HH:mm:ss z yyyy",
        "^[a-zA-Z0-9 ]{10,11} \\d{2}:\\d{2}:\\d{2} CST \\d{4}$"),
    ;
    /**
     * 日期格式化规则
     */
    private String pattern;
    /**
     * 字符串匹配规则（简单实现）
     */
    private String matche;

    DatePatternEnum(String pattern, String matche) {
      this.pattern = pattern;
      this.matche = matche;
    }

    public String getPattern() {
      return pattern;
    }

    public String getMatche() {
      return matche;
    }
  }

}
