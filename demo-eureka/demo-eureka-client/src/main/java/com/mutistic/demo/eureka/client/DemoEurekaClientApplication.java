package com.mutistic.demo.eureka.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
// 开启Eureka客户端功能
@EnableEurekaClient
public class DemoEurekaClientApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoEurekaClientApplication.class, args);
  }
}