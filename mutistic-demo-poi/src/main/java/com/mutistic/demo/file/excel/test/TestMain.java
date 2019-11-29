package com.mutistic.demo.file.excel.test;

import com.mutistic.demo.file.constants.FileTypeEnum;
import com.mutistic.demo.file.excel.function.ReadExcel;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yinyc
 * @version 1.0 2019/11/28
 */
@Slf4j
public class TestMain {

  public static void main(String[] args) {
    String path = TestMain.class.getResource("").getPath()
        .replace("file:/", "")
        .replace("target/classes", "src/main/java");

    read(path, "empty.xlsx");
    read(path, "test-annontation.xlsx");
    read(path, "test-name.xlsx");
  }

  private static void read(String path, String fileName) {
    try {
      log.info("===开始读{}文件===", fileName);
      InputStream inputStream = new FileInputStream(path + fileName);
      List<TestEntity> list = new ReadExcel(inputStream, FileTypeEnum.XSSF)
          .read(TestEntity.class);
      log.info("===========结果{}", list);
    } catch (Exception e) {
      log.error("exception：", e);
    }
    System.out.println();
  }

}
