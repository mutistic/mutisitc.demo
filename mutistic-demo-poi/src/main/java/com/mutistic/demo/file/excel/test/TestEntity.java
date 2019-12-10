package com.mutistic.demo.file.excel.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mutistic.demo.file.annotation.ExcelColumn;
import java.util.Date;
import lombok.Data;

@Data
public class TestEntity {

  @ExcelColumn(name="id")
  private Long id;
  @ExcelColumn(name = "名称")
  private String name;
  @ExcelColumn(name = "值")
  private String value;
  @ExcelColumn(name = "日期字符串")
  private String time1;
  @ExcelColumn(name = "日期字符串")
  @JsonFormat(pattern = "yyyy-MM-DD HH:mi:ss")
  private String time2;
}
