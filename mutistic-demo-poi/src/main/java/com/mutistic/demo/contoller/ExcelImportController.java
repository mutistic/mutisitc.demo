package com.mutistic.demo.contoller;

import com.mutistic.demo.file.constants.FileTypeEnum;
import com.mutistic.demo.file.excel.function.ReadExcel;
import com.mutistic.demo.file.excel.test.TestEntity;
import com.mutistic.demo.file.utils.ExcelUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * @author yinyc
 * @version 1.0 2019/11/28
 */
@RestController
@RequestMapping("/excel")
public class ExcelImportController {

  @PostMapping("/upload")
  public List<TestEntity> upload(HttpServletRequest request) throws IOException {
    List<TestEntity> list = new ArrayList<>();
    if (!(request instanceof MultipartRequest)) {
      return list;
    }
    MultipartRequest multipartRequest = (MultipartRequest) request;
    for (Entry<String, MultipartFile> entry : multipartRequest.getFileMap().entrySet()) {
      System.out.println(entry.getValue().getInputStream());
      FileTypeEnum type = ExcelUtil.getFileEnum(entry.getValue());
      if (type == null) {
        continue;
      }
      list.addAll(new ReadExcel(entry.getValue().getInputStream(), type).read(TestEntity.class));
    }
    return list;
  }

}
