package com.mutistic.demo.eureka.server.config;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    //关闭csrf，加这句是为了访问eureka控制台和/actuator时能做安全控制
    http.csrf().ignoringAntMatchers("/eureka/**");
//    http.csrf().disable();
  }
}
