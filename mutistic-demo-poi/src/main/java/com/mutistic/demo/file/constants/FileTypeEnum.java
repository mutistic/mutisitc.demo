package com.mutistic.demo.file.constants;

/**
 * 文件类型 枚举
 *
 * @author yinyc
 * @version 1.0 2019/11/21
 */
public enum FileTypeEnum {
  /**
   * HSSF：POI操作Excel97-2003版本 > xls文件
   */
  HSSF("xls", ".xls"),
  /**
   * XSSF：POI操作Excel2007版本开始 > xlsx文件
   */
  XSSF("xlsx", ".xlsx"),
  /**
   * SXSSF：POI针对大数据量 > xlsx文件
   */
  SXSSF("xlsx", ".xlsx"),
  /**
   * CSV > csv文件
   */
  CSV("csv", ".csv"),
  ;

  FileTypeEnum(String key, String value) {
    this.key = key;
    this.value = value;
  }

  private String key;
  private String value;

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public static FileTypeEnum getEnum(String key) {
    for (FileTypeEnum temp : values()) {
      if (temp.getKey().equals(key)) {
        return temp;
      }
    }
    return null;
  }
}
