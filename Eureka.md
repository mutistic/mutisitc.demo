## 一、Eureka 依赖配置 > pom.xml
1.1、基于Spring Cloud的依赖说明：  
- [x] 起步启动依赖：Spring-Boot：2.2.2.RELEASE
```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.2.2.RELEASE</version>
  <relativePath/> <!-- lookup parent from repository -->
</parent>
```
- [x] 微服务依赖：Spring-Cloud：Hoxton.RELEASE
```xml
<properties>
  <java.version>1.8</java.version>
  <spring-cloud.version>Hoxton.RELEASE</spring-cloud.version>
</properties>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>${spring-cloud.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```
1.2、Eureka服务端启动依赖：eureka-server：1.9.13
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
1.2.2、Eureka服务端依赖关系递进：
> spring-cloud-starter-netflix-eureka-server-2.2.0.RELEASE
> > spring-cloud-netflix-eureka-server-2.2.0.RELEASE
> > > eureka-client-1.9.13   
eureka-core-1.9.13  
spring-cloud-netflix-eureka-client-2.2.0.RELEASE  

1.3、Eureka客户端启动依赖：eureka-client：1.9.13
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
1.3.2、Eureka客户端依赖关系递进：
> spring-cloud-starter-netflix-eureka-client-2.2.0.RELEASE
> > spring-cloud-netflix-eureka-client-2.2.0.RELEASE
> > > eureka-client-1.9.13   
eureka-core-1.9.13  

## 二、Eureka 属性配置 > application.yml
2.1、Eureka实例配置属性：

eureka.instance|org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean
---|---
prefer-ip-address|标识是否使用ip地址作为主机名称，默认false-不使用
ip-address|实例的ip地址
hostname|实例的主机名称
instance-id|实例自身名称

2.1.1、属性说明：  
```
1、经测试在Spring-Cloud：Hoxton.RELEASE版本中，属性配置使用驼峰命名或使用 - 命名一样会调用setXXX方法，注入属性，
即ip-address和 ipAddress效果是一样的。
2、prefer-ip-address + ip-address 可以代替 hostname
ip-address 和 hostname 均不配置，实例名称会被默认注册成主机名称，
可能Eureka定时检测服务出现错误，访问失败
```
2.1.2、属性的配置决定注册Eureka实例的行为：
```
InstanceInfoFactory.create() >
EurekaConfigBasedInstanceInfoProvider.get() >
PropertiesInstanceConfig.getInstanceId()
```
`EurekaConfigBasedInstanceInfoProvider.java`部分代码：
```java
public synchronized InstanceInfo get() {
  String instanceId = config.getInstanceId();
  if (instanceId == null || instanceId.isEmpty()) {
    DataCenterInfo dataCenterInfo = config.getDataCenterInfo();
    if (dataCenterInfo instanceof UniqueIdentifier) {
      instanceId = ((UniqueIdentifier) dataCenterInfo).getId();
    } else {
      instanceId = config.getHostName(false);
    }
  }
}
```
`PropertiesInstanceConfig.java`部分代码：
```java
public String getInstanceId() {
  String result = configInstance.getStringProperty(namespace + INSTANCE_ID_KEY, null).get();
  return result == null ? null : result.trim();
}
```

2.2、Eureka服务器配置属性：
- [ ] eureka.server

2.3、Eureka客户端配置属性：
- [x] eureka.client

eureka.client|org.springframework.cloud.netflix.eureka.EurekaClientConfigBean
---|---
fetch-registry|标识是否检索服务：默认true-检索。
register-with-eureka|标识是否向Eureka注册自己：默认true-注册自己
service-url.defaultZone|Eureka服务端全地址Map，defaultZone为默认key，多服务用,隔开（不能有空格）

2.3.1、属性说明：
```
1、Eureka单服务端设置：（因为只有单服务，所以服务端并不用注册自己）
fetch-registry=false，register-with-eureka=false，service-url不配置
2、Eureka多服务端设置：（多服务端的情况下是把自身作为客户端像其他的服务端注册自己）
fetch-registry=true，register-with-eureka=true
3、Eureka客户端设置：（客户端不需要开启检索服务，只需要向服务端注册自己，多服务端会自动同步）
fetch-registry=false，register-with-eureka=true,service-url.defaultZone配置为Eureka服务端url全路径，

4、service-url特殊说明：
多个服务用 , 隔开（url中不能有空格）
全路径中支持${key}操作，但是key必须存在，否则会认为${key}是字符串而不是变量，但是启动并不会失败。
全路径支持用户名和密码登录。
defaultZone为Eureka服务端的MAP集合的默认key值，
比如：
eureka:
  client:
    service-url:
      defaultZone: http://${spring.security.user.name}:${spring.security.user.password}@server2.com:8762/eureka,http://${spring.security.user.name}:${spring.security.user.password}@server3.com:8763/eureka
```

## 三、Eureka 详细配置：
3.1、单Eureka服务端配置：  
`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
  <groupId>com.mutistic.demo</groupId>
  <artifactId>demo-eureka-server</artifactId>
  <version>1.0</version>
  <name>demo-eureka-server</name>
  <description>eureka服务发现demo</description>

  <properties>
    <java.version>1.8</java.version>
    <spring-cloud.version>Hoxton.RELEASE</spring-cloud.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
  </dependencies>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```
`application.yml`
```yaml
spring:
  application:
    name: demo-eureka-server

logging:
  level.root: debug

# Eureka单服务配置
server:
  port: 9080

eureka:
  instance:
    prefer-ip-address: true
    ip-address: 127.0.0.1
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/${server.servlet.context-path}
```
`Application.java` `@EnableEurekaServer`
```java
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
```

3.2、多Eureka服务端配置
`pom.xml`依赖配置同3.1的`pom.xml`   
`application.yml`
```yaml
spring:
  application:
    name: demo-eureka-server

logging:
  level.root: debug
```
`application-server1.yml`
```yaml
server:
  port: 8761

eureka:
  instance:
    hostname: server1.com
    # eureka实例名称
    instance-id: server1
  client:
    fetch-registry: true #标识是否检索服务：默认true-检索
    register-with-eureka: true #标识是否向Eureka注册自己：默认true-注册自己
    service-url:
      # defaultZone 默认
#      defaultZone: http://server2.com:8762/eureka,http://server3.com:8763/eureka
      defaultZone: http://${spring.security.user.name}:${spring.security.user.password}@server2.com:8762/eureka,http://${spring.security.user.name}:${spring.security.user.password}@server3.com:8763/eureka
#      defaultZone: http://peer1:8761${server.servlet.context-path}/eureka
```
`application-server2.yml`
```yaml
server:
  port: 8762

eureka:
  instance:
    hostname: server2.com
    instance-id: server2
  client:
    fetch-registry: true #是否检索服务：默认true-检索
    register-with-eureka: true #表示向Eureka注册自身服务：默认true-注册自身
    service-url:
      # defaultZone 默认
#      defaultZone: http://server1.com:8761/eureka,http://server3.com:8763/eureka
      defaultZone: http://${spring.security.user.name}:${spring.security.user.password}@server1.com:8761/eureka,http://${spring.security.user.name}:${spring.security.user.password}@server3.com:8763/eureka
#      defaultZone: http://peer1:8761${server.servlet.context-path}/eureka
```
`application-server3.yml`
```yaml
server:
  port: 8763

eureka:
  instance:
    hostname: server3.com
    instance-id: server3
  client:
    fetch-registry: true #是否检索服务：默认true-检索
    register-with-eureka: true #表示向Eureka注册自身服务：默认true-注册自身
    service-url:
      # defaultZone 默认
#      defaultZone: http://server1.com:8761/eureka,http://server2.com:8762/eureka
      defaultZone: http://${spring.security.user.name}:${spring.security.user.password}@server1.com:8761/eureka,http://${spring.security.user.name}:${spring.security.user.password}@server2.com:8762/eureka
#      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
```
`Application.java`依赖配置同3.1的`Application.java`   
本地模拟多服务域名的hosts配置：`C:\Windows\System32\drivers\etc\hosts`   
```
127.0.0.1 server1.com
127.0.0.1 server2.com
127.0.0.1 server3.com
# 或使用下面的配置
# 127.0.0.1 server1.com server2.com server3.com
```
本地多服务启动方式：  
IDEA启动配置：
```
Run/Debug Configurations > Spring Boot > Main Class=Application > Active profies=server1
Run/Debug Configurations > Spring Boot > Main Class=Application > Active profies=server2
Run/Debug Configurations > Spring Boot > Main Class=Application > Active profies=server3  
```
Java -jar 启动方式：   
先通过maven插件打包成jar包后，进入jar包所在目录：   
```shell script
java -jar demo-eureka-server-1.0.jar --spirng.profies.active=server1
java -jar demo-eureka-server-1.0.jar --spirng.profies.active=server2
java -jar demo-eureka-server-1.0.jar --spirng.profies.active=server3
```
3.3、Eureka客户端配置：  
`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
  <groupId>com.mutistic.demo</groupId>
  <artifactId>demo-eureka-server</artifactId>
  <version>1.0</version>
  <name>demo-eureka-server</name>
  <description>eureka服务发现demo</description>

  <properties>
    <java.version>1.8</java.version>
    <spring-cloud.version>Hoxton.RELEASE</spring-cloud.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
  </dependencies>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```
`application.yml`
```yaml
spring:
  application:
    name: demo-eureka-server

logging:
  level.root: debug

# Eureka单服务配置
server:
  port: 9080

eureka:
  instance:
    prefer-ip-address: true
    ip-address: 127.0.0.1
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/${server.servlet.context-path}
```
`Application.java`
```java
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
```
![](demo-eureka/eureka-server-2.png)
