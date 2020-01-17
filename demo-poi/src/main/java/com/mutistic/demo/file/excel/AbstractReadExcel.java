package com.mutistic.demo.file.excel;

import com.mutistic.demo.file.constants.FileTypeEnum;
import com.mutistic.demo.file.utils.ExcelUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取 Excel 抽象类
 *
 * @author yinyc
 * @version 1.0 2019/11/27
 */
@Slf4j
public abstract class AbstractReadExcel {

  /**
   * Workbook
   */
  private Workbook workbook;
  /**
   * 输入流
   */
  private InputStream inputStream;

  public Workbook getWorkbook() {
    return workbook;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public AbstractReadExcel(InputStream inputStream, FileTypeEnum fileType) throws IOException {
    this.inputStream = inputStream;
    initWorkbook(fileType);
  }

  /**
   * 初始化 Workbook
   *
   * @param fileType 文件类型枚举
   * @throws IOException IOException
   */
  public void initWorkbook(FileTypeEnum fileType) throws IOException {
    if (FileTypeEnum.HSSF == fileType) {
      this.workbook = new HSSFWorkbook(this.inputStream);
    } else if (FileTypeEnum.XSSF == fileType) {
      this.workbook = new XSSFWorkbook(this.inputStream);
    }
  }

  /**
   * 读取Excel
   *
   * @param <T>   类类型
   * @param clazz 类
   * @return 读取结果
   */
  public abstract <T> List<T> read(Class<T> clazz);

  /**
   * 获取表头集合
   *
   * @param sheet 页签
   * @return 表头集合
   */
  public List<String> readHead(Sheet sheet) {
    log.info("sheet.getLastRowNum()={}", sheet.getLastRowNum());
    if (sheet.getLastRowNum() == 0) {
      return null;
    }
    Row head = sheet.getRow(0);
    if (head == null) {
      return null;
    }
    int cellNum = head.getLastCellNum();
    log.info("row.getLastCellNum()={}", cellNum);
    if (cellNum == 0) {
      return null;
    }
    List<String> headList = new ArrayList<>(cellNum);
    for (int i = 0; i < cellNum; i++) {
      Object cellValue = ExcelUtil.getCellValue(head.getCell(i));
      if (!(cellValue instanceof String)) {
        headList.add("null-" + i);
      } else {
        headList.add((String) cellValue);
      }
    }
    return headList;
  }

  public abstract <T> List<T> readRow(Sheet sheet, Class<T> clazz);

  public abstract <T> T readCell(Row row, Class<T> clazz)
      throws IllegalAccessException, InstantiationException;

  /**
   * 关闭资源
   *
   * @param inputStream 输入流
   * @throws IOException IOException
   */
  public void close(InputStream inputStream) throws IOException {
    if (this.workbook != null) {
      this.workbook.close();
    }
    if (inputStream != null) {
      inputStream.close();
    }
  }

}
