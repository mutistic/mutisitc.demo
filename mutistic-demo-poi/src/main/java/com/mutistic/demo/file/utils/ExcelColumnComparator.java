package com.mutistic.demo.file.utils;

import com.mutistic.demo.file.annotation.ExcelColumn;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Map.Entry;

/**
 * ExcelColumn Comparator 排序规则
 *
 * @author mutistic
 * @date 2019/8/21 10:50
 */
public class ExcelColumnComparator implements Comparator<Entry<ExcelColumn, Field>> {

  @Override
  public int compare(Entry<ExcelColumn, Field> column1, Entry<ExcelColumn, Field> column2) {
    int sort1 = 0;
    int sort2 = 0;
    if (column1 != null && column1.getKey() != null) {
      sort1 = column1.getKey().sort();
    }
    if (column2 != null && column2.getKey() != null) {
      sort2 = column2.getKey().sort();
    }
    return sort1 - sort2;
  }
}
