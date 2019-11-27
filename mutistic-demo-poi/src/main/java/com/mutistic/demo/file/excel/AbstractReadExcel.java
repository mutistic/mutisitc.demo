package com.mutistic.demo.file.excel;

import com.mutistic.demo.file.constants.FileTypeEnum;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 读取 Excel 抽象类
 *
 * @author yinyc
 * @version 1.0 2019/11/27
 */
public abstract class AbstractReadExcel {

  /**
   * Workbook
   */
  public Workbook workbook;
  /**
   * 输入流
   */
  public InputStream inputStream;

  /**
   * 获取实例
   *
   * @param multipartFile multipartFile
   * @return AbstractExcelRead 实例
   * @throws IOException IOException
   */
  public abstract AbstractReadExcel getInstance(MultipartFile multipartFile, FileTypeEnum fileType)
      throws IOException;

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
   * @param multipartFile 文件
   * @param clazz         类
   * @param <T>           类类型
   * @return 读取结果
   */
  public abstract <T> List<T> read(MultipartFile multipartFile, Class<T> clazz);

  /**
   * 并行读取Excel
   *
   * @param multipartFile 文件
   * @param clazz         类
   * @param <T>           类类型
   * @return 读取结果
   */
  public abstract <T> List<T> parallelRead(MultipartFile multipartFile, Class<T> clazz);

  public List<String> getHeadList(Sheet sheet) {
    if (sheet.getLastRowNum() == 0) {
      return null;
    }
    Row head = sheet.getRow(0);
    if (head.getLastCellNum() == 0) {
      return null;
    }
    List<String> headList = new ArrayList<>();
    Cell cell;
    for (int i = 0; i < head.getLastCellNum(); i++) {
      cell = head.getCell(i);
      if (cell == null) {
        headList.add("null-" + i);
        continue;
      }
      headList.add(cell.getStringCellValue());
    }

    return headList;
  }

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
