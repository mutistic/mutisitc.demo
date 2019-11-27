package com.mutistic.demo.file.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 列 注解
 * <p> 1、作用于字段
 *
 * @author yinyc
 * @version 1.0 2019/11/27
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
//  /**
//   * 列Key
//   *
//   * @return 列名
//   */
//  String key() default "";

  /**
   * 列名
   *
   * @return 列名
   */
  String name();

  /**
   * 排序
   *
   * @return 排序
   */
  int sort() default -1;

  /**
   * 格式化规则
   *
   * @return 格式化规则
   */
  String format() default "";

  /**
   * 字符长度 > 默认列宽=字符长度*20
   *
   * @return 字符长度
   */
  int length() default -1;

  /**
   * 所属组
   *
   * @return 组名
   */
  String groupName() default "";
}
