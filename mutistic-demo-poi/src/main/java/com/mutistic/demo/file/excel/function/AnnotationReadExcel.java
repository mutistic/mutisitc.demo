package com.mutistic.demo.file.excel.function;

import com.mutistic.demo.file.annotation.ExcelColumn;
import com.mutistic.demo.file.constants.FileTypeEnum;
import com.mutistic.demo.file.excel.AbstractReadExcel;
import com.mutistic.demo.file.utils.ExcelUtil;
import com.mutistic.demo.utils.CommonUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通过注解读取 Excel
 *
 * @author yinyc
 * @version 1.0 2019/11/27
 */
@Slf4j
public class AnnotationReadExcel extends AbstractReadExcel {

  /**
   * Excel 列关系集合
   */
  private Map<ExcelColumn, Field> columnMap;

  @Override
  public AbstractReadExcel getInstance(MultipartFile multipartFile, FileTypeEnum fileType)
      throws IOException {
    AbstractReadExcel excelRead = new AnnotationReadExcel();
    excelRead.inputStream = multipartFile.getInputStream();
    excelRead.initWorkbook(fileType);
    return excelRead;
  }

  @Override
  public <T> List<T> read(MultipartFile multipartFile, Class<T> clazz) {
    List<T> dataList = new ArrayList<>();
    columnMap = ExcelUtil.getColumnMap(clazz);
    if (MapUtils.isEmpty(columnMap)) {
      return dataList;
    }
    //默认读取第一个sheet
    for (int i = 1; i <= workbook.getNumberOfSheets(); i++) {
      Sheet sheet = workbook.getSheetAt(i - 1);
//      List<ExcelColumn> columnList = FileUtil.getExcelColumnList(columnMap.keySet(), super.getHeadList(sheet));
    }

    return dataList;
  }

  @Override
  public <T> List<T> parallelRead(MultipartFile multipartFile, Class<T> clazz) {
    return null;
  }
}