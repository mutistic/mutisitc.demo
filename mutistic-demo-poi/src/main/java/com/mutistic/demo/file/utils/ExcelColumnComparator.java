package com.mutistic.demo.file.utils;

import com.mutistic.demo.file.annotation.ExcelColumn;
import java.util.Comparator;

/**
 * Excel Column Comparator 排序规则
 *
 * @author yinyc
 * @date 2019/8/21 10:50
 */
public class ExcelColumnComparator implements Comparator<ExcelColumn> {

  @Override
  public int compare(ExcelColumn column1, ExcelColumn column2) {
    return column1.sort() - column2.sort();
  }
}
