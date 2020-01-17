package com.mutistic.demo.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
// 开启Eureka服务端
@EnableEurekaServer
public class DemoEurekaServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoEurekaServerApplication.class, args);
  }
}
