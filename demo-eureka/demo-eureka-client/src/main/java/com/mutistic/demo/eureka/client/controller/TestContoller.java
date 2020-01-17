package com.mutistic.demo.eureka.client.controller;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.UniqueIdentifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContoller {

  @GetMapping("/hello")
  public String hello() {
    return "success";
  }
}
