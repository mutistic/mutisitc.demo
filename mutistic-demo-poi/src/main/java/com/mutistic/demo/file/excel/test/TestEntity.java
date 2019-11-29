package com.mutistic.demo.file.excel.test;

import com.mutistic.demo.file.annotation.ExcelColumn;
import lombok.Data;

@Data
public class TestEntity {

  @ExcelColumn(name = "名称")
  private String name;
  @ExcelColumn(name = "值")
  private String value;

  @Override
  public String toString() {
    return "TestEntity{" +
        "name='" + name + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
