package com.mutistic.demo.file.excel.function;

import com.mutistic.demo.file.constants.FileTypeEnum;
import com.mutistic.demo.file.excel.AbstractReadExcel;
import com.mutistic.demo.file.utils.ExcelUtil;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 根据类字段读取 Excel
 * <p> 1、要求表头的值和类字段名称一致
 * <p> 2、字段数据类型要求为public
 *
 * @author yinyc
 * @version 1.0 2019/11/28
 */
@Slf4j
public class ReadExcel extends AbstractReadExcel {

  /**
   * 当前页 表头与字段的关联Map
   * <p>key=cellIndex， value=字段
   */
  private Map<Integer, Field> headFieldMap;
  /**
   * 当前页 表头与set方法的关联Map
   * <p>key=cellIndex， value=set方法
   */
  private Map<Integer, Method> headMethodSetMap;

  public ReadExcel(InputStream inputStream,
      FileTypeEnum fileType) throws IOException {
    super(inputStream, fileType);
  }

  @Override
  public <T> List<T> read(Class<T> clazz) {
    List<T> dataArray = new ArrayList<>();
    Sheet sheet;
    for (int sheetIndex = 0; sheetIndex < getWorkbook().getNumberOfSheets(); sheetIndex++) {
      sheet = getWorkbook().getSheetAt(sheetIndex);
      // 优先通过注解获取表头集合
      initHeadAnnotationMap(sheet, clazz);
      if (MapUtils.isEmpty(headFieldMap)) {
        // 其次通过字段名获取表头集合
        initHeadFiledMap(sheet, clazz);
      }
      // 没有符合的字段
      if (MapUtils.isEmpty(headFieldMap)) {
        log.warn("没有要读取的字段");
        continue;
      }
      List<T> temp = this.readRow(sheet, clazz);
      if (CollectionUtils.isNotEmpty(temp)) {
        dataArray.addAll(temp);
      }
    }
    return dataArray;
  }

  @Override
  public <T> List<T> readRow(Sheet sheet, Class<T> clazz) {
    // 索引从0开始
    int rowNum = sheet.getLastRowNum();
    log.info("sheet.getLastRowNum()={}", rowNum);
    if (rowNum <= 1) {
      return null;
    }
    List<T> dataArray = new ArrayList<>(rowNum);
    // 读行 row
    for (int rowIndex = 1; rowIndex <= rowNum; rowIndex++) {
      dataArray.add(readCell(sheet.getRow(rowIndex), clazz));
    }
    return dataArray;
  }

  @Override
  public <T> T readCell(Row row, Class<T> clazz) {
    try {
      int cellNum = row == null ? 0 : row.getLastCellNum();
      log.info("row.getLastCellNum()={}", cellNum);
      if (row == null || cellNum < 1) {
        return null;
      }
      // 读单元格 cell
      T data = clazz.newInstance();
      String value;
      Field field;
      for (int cellIndex = 0; cellIndex < cellNum; cellIndex++) {
        field = headFieldMap.get(cellIndex);
        if (field == null) {
          continue;
        }
        value = ExcelUtil.getCellValue(row.getCell(cellIndex));
        if (value == null) {
          continue;
        }
        try {
          field.set(data, ExcelUtil.stringToType(value, field.getType()));
        } catch (IllegalAccessException e) {
          log.error(e.getMessage());
          // 对File字段没有权限 转掉Method方法实现
          Method method = getMethodByFiled(cellIndex, field, clazz, false);
          if (method != null) {
            method.invoke(data, ExcelUtil.stringToType(value, field.getType()));
          }
        }
      }
      return data;
    } catch (Exception e) {
      log.error("exception-readExcel", e);
    }
    return null;
  }

  /**
   * 根据表头获取类的字段集合
   * <p> 表头值为类的字段名称
   *
   * @param sheet 当前页签
   * @param clazz 类
   * @param <T>   类类型
   */
  private <T> void initHeadFiledMap(Sheet sheet, Class<T> clazz) {
    List<String> headList = readHead(sheet);
    if (CollectionUtils.isEmpty(headList)) {
      return;
    }

    headFieldMap = new HashMap<>(headList.size());
    Map<String, Field> fieldMap = ExcelUtil.getFieldMap(clazz);
    Field field;
    for (int i = 0; i < headList.size(); i++) {
      field = fieldMap.get(headList.get(i));
      if (field == null) {
        continue;
      }
      headFieldMap.put(i, field);
    }
  }

  /**
   * 根据表头+注解获取类的字段集合
   * <p> 表头值为注解的Name
   *
   * @param sheet 当前页签
   * @param clazz 类
   * @param <T>   类类型
   */
  private <T> void initHeadAnnotationMap(Sheet sheet, Class<T> clazz) {
    List<String> headList = readHead(sheet);
    if (CollectionUtils.isEmpty(headList)) {
      return;
    }

    headFieldMap = new HashMap<>(headList.size());
    Map<String, Field> fieldMap = ExcelUtil.getFieldMapByAnnontation(clazz);
    Field field;
    for (int i = 0; i < headList.size(); i++) {
      field = fieldMap.get(headList.get(i));
      if (field == null) {
        continue;
      }
      headFieldMap.put(i, field);
    }
  }

  /**
   * 获取Field对应的Method方法
   *
   * @param key         headFiledMap.key
   * @param field       字段
   * @param clazz       类
   * @param <T>         类类型
   * @param readOrWrite true： ReadMethod，false：WriteMethod
   * @return Filed对应的Method
   */
  private <T> Method getMethodByFiled(int key, Field field, Class<T> clazz, boolean readOrWrite) {
    if (MapUtils.isEmpty(headMethodSetMap)) {
      headMethodSetMap = new HashMap<>(headFieldMap.size());
    }
    Method method = headMethodSetMap.get(key);
    if (method == null) {
      try {
        method = readOrWrite ? new PropertyDescriptor(field.getName(), clazz).getReadMethod()
            : new PropertyDescriptor(field.getName(), clazz).getWriteMethod();
      } catch (IntrospectionException e) {
        log.error(e.getMessage());
      }
      headMethodSetMap.put(key, method);
    }
    return method;
  }

}
