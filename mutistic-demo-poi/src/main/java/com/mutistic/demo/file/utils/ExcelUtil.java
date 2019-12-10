package com.mutistic.demo.file.utils;

import com.mutistic.demo.file.annotation.ExcelColumn;
import com.mutistic.demo.file.annotation.ExcelSheet;
import com.mutistic.demo.file.constants.FileConstant;
import com.mutistic.demo.file.constants.FileTypeEnum;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 *
 * @author yinyc
 * @version 1.0 2019/11/27
 */
public class ExcelUtil {

  public static void main(String[] args) {
    String ss = "";
    System.out.println(NumberUtils.toDouble(ss, 0));
  }

  /**
   * 根据文件名获取文件类型
   *
   * @param fileName 文件名
   * @return 文件类型枚举
   */
  public static FileTypeEnum getFileEnum(String fileName) {
    if (StringUtils.isBlank(fileName) || !fileName.contains(FileConstant.POINT)) {
      return null;
    }
    String suffix = fileName.substring(fileName.lastIndexOf(FileConstant.POINT) + 1);
    return FileTypeEnum.getEnum(suffix);
  }

  /**
   * 根据MultipartFile获取文件类型
   *
   * @param file MultipartFile
   * @return 文件类型枚举
   * @throws IOException IOException
   */
  public static FileTypeEnum getFileEnum(MultipartFile file) throws IOException {
    if (file == null) {
      return null;
    }
    String fileName = file.getOriginalFilename();
    if (StringUtils.isBlank(fileName)) {
      return null;
    }
    InputStream stream = file.getInputStream();
    if (stream == null) {
      return null;
    }
    return ExcelUtil.getFileEnum(fileName);
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
   * 获取类的字段集合
   *
   * @param clazz 类
   * @param <T>   类类型
   * @return key：字段名，value=字段实例
   */
  public static <T> Map<String, Field> getFieldMap(Class<T> clazz) {
    Map<String, Field> fieldMap = new HashMap<>();
    Field[] fields = clazz.getDeclaredFields();
    Field field;
    for (int i = 0; i < fields.length; i++) {
      field = fields[i];
      fieldMap.put(field.getName(), field);
    }
    return fieldMap;
  }

  /**
   * 获取类的字段和注解的关系集合（不排序）
   *
   * @param clazz 包含ExcelColumn注解的类
   * @param <T>   类类型
   * @return key=注解名，value=Filed
   */
  public static <T> Map<String, Field> getFieldMapByAnnontation(Class<T> clazz) {
    if (clazz == null) {
      return null;
    }
    Field[] fieldArray = clazz.getDeclaredFields();
    Map<String, Field> columnMap = new HashMap<>(fieldArray == null ? 0 : fieldArray.length);

    ExcelColumn column;
    for (Field field : fieldArray) {
      column = field.getAnnotation(ExcelColumn.class);
      if (column == null || StringUtils.isBlank(column.name())) {
        continue;
      }
      columnMap.put(column.name(), field);
    }
    return columnMap;
  }

  /**
   * 获取类的字段和注解的关系集合（不排序）
   *
   * @param clazz 包含ExcelColumn注解的类
   * @param <T>   类类型
   * @return key=ExcelColumn，value=Filed
   */
  public static <T> Map<ExcelColumn, Field> getAnnotationMap(Class<T> clazz) {
    if (clazz == null) {
      return null;
    }
    Field[] fieldArray = clazz.getDeclaredFields();
    Map<ExcelColumn, Field> columnMap = new HashMap<>(fieldArray == null ? 0 : fieldArray.length);

    ExcelColumn column;
    for (Field field : fieldArray) {
      column = field.getAnnotation(ExcelColumn.class);
      if (column == null || StringUtils.isBlank(column.name())) {
        continue;
      }
      columnMap.put(column, field);
    }
    return columnMap;
  }

  /**
   * 获取类的字段和注解的关系集合（排序）
   *
   * @param clazz 包含ExcelColumn注解的类
   * @param <T>   类类型
   * @return key=ExcelColumn，value=Filed
   */
  public static <T> Map<ExcelColumn, Field> getAnnotationSortMap(Class<T> clazz) {
    Map<ExcelColumn, Field> columnMap = getAnnotationMap(clazz);
    if (MapUtils.isEmpty(columnMap)) {
      return columnMap;
    }
    Map<ExcelColumn, Field> sortMap = new LinkedHashMap<>(columnMap.size());
    List<Entry<ExcelColumn, Field>> entryList = new ArrayList<>(columnMap.entrySet());
    Collections.sort(entryList, FileConstant.COMPARATOR_EXCEL_COLUMN);
    for (Entry<ExcelColumn, Field> entry : entryList) {
      sortMap.put(entry.getKey(), entry.getValue());
    }
    return sortMap;
  }

  /**
   * 获取单元格值
   * <p> 处理成String字符串
   *
   * @param cell 单元格
   * @return 返回字符串
   */
  public static String getCellValue(Cell cell) {
    if (cell == null) {
      return "";
    }
    if (cell.getCellType() == CellType.STRING) {
      return StringUtils.trimToEmpty(cell.getStringCellValue());
    } else if (cell.getCellType() == CellType.NUMERIC) {
      if (DateUtil.isCellDateFormatted(cell)) {
        return cell.getDateCellValue().toString();// HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
      } else {
        return new BigDecimal(cell.getNumericCellValue()).toString();
      }
    } else if (cell.getCellType() == CellType.FORMULA) {
      return StringUtils.trimToEmpty(cell.getCellFormula());
    } else if (cell.getCellType() == CellType.BLANK) {
      return "";
    } else if (cell.getCellType() == CellType.BOOLEAN) {
      return String.valueOf(cell.getBooleanCellValue());
    } else if (cell.getCellType() == CellType.ERROR) {
      return "ERROR";
    } else {
      return cell.toString().trim();
    }
  }

  /**
   * 将Source转换成目标类型数据
   *
   * @param source String data
   * @param type   目标类型
   * @param <T>    类型
   * @return 目标类型数据
   */
  public static <T> Object stringToType(String source, Class<T> type) {
    if (type == null || type == void.class || source == null) {
      return null;
    }
    if (type == String.class) {
      return source;
    }
    if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
      if (type == int.class || type == Integer.class) {
        return NumberUtils.toInt(source);
      }
      if (type == long.class || type == Long.class) {
        return NumberUtils.toLong(source);
      }
      if (type == byte.class || type == Byte.class) {
        return NumberUtils.toByte(source);
      }
      if (type == short.class || type == Short.class) {
        return NumberUtils.toShort(source);
      }
      if (type == double.class || type == Double.class) {
        return NumberUtils.toDouble(source);
      }
      if (type == float.class || type == Float.class) {
        return NumberUtils.toFloat(source);
      }
      if (type == char.class || type == Character.class) {
        return CharUtils.toChar(source);
      }
      if (type == boolean.class) {
        return BooleanUtils.toBoolean(source);
      }
      if (type == BigDecimal.class) {
        return new BigDecimal(source);
      }
    }
    if (type == Boolean.class) {
      return BooleanUtils.toBoolean(source);
    }
    if (type == Date.class) {
      return new Date(source);
    }
    if (type == Object.class) {
      return source;
    }
//    else {
//      Constructor<?> constructor = type.getConstructor(String.class);
//      return  constructor.newInstance(value);
//    }
    return null;
  }
}
