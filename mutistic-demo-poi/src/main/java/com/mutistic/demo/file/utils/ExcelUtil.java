package com.mutistic.demo.file.utils;

import com.mutistic.demo.file.annotation.ExcelColumn;
import com.mutistic.demo.file.annotation.ExcelSheet;
import com.mutistic.demo.file.constants.FileConstant;
import com.mutistic.demo.file.constants.FileTypeEnum;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件工具类
 *
 * @author yinyc
 * @version 1.0 2019/11/27
 */
public class ExcelUtil {

  public static void main(String[] args) {
    System.out.println(getFileEnum("1.xls"));
  }

  public static FileTypeEnum getFileEnum(String fileName) {
    if (StringUtils.isBlank(fileName) || !fileName.contains(FileConstant.POINT)) {
      return null;
    }
    String suffix = fileName.substring(fileName.lastIndexOf(FileConstant.POINT) + 1);
    return FileTypeEnum.getEnum(suffix);
  }

  /**
   * 获取 Excel 页签名
   *
   * @param clazz 包含ExcelSheet注解的类
   * @param <T>   类类型
   * @return 页签名
   */
  public static <T> String getSheetName(Class<T> clazz) {
    if (clazz == null) {
      return null;
    }
    ExcelSheet sheet = clazz.getAnnotation(ExcelSheet.class);
    if (sheet == null && StringUtils.isEmpty(sheet.name())) {
      return null;
    }
    return sheet.name();
  }

  /**
   * 获取 Excel Column与列注解的关联Map
   * <p> key=ExcelColumn，value=Filed
   *
   * @param <T>   类类型
   * @param clazz 包含ExcelColumn注解的类
   * @return 关联Map
   */
  public static <T> Map<ExcelColumn, Field> getColumnMap(Class<T> clazz) {
    if (clazz == null) {
      return null;
    }
    Field[] fieldArray = clazz.getDeclaredFields();
    Map<ExcelColumn, Field> columnMap = new HashMap<>(fieldArray.length);
    List<Map.Entry<ExcelColumn, Field>> columnList = new ArrayList<>();
//
//
//    Map<ExcelColumn, Field> columnMap = new HashMap<>(fieldArray.length);
//    List<ExcelColumn> columnList = new ArrayList<>();
//    ExcelColumn excelField;
//    for (Field field : fieldArray) {
//      excelField = field.getAnnotation(ExcelColumn.class);
//      if (excelField != null) {
//        columnMap.put(excelField, field);
//        columnList.add(excelField);
//      }
//    }
//    if (CommonUtils.isEmpty(columnList)) {
//      return columnMap;
//    }
//    Collections.sort(columnList, FileConstant.COMPARATOR_EXCEL_COLUMN);
//    Map<ExcelColumn, Field> filedMap = new LinkedHashMap<>(columnList.size());
//    for (ExcelColumn column : columnList) {
//      filedMap.put(column, columnMap.get(column));
//    }
    return columnMap;
  }

  public static List<ExcelColumn> getExcelColumnList(List<String> headList) {
    List<ExcelColumn> columnList = new ArrayList<>();
    return columnList;
  }
}
