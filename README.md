## API PLATFORM BACKEND



### 项目介绍

API开放平台后端 一个提供 API接口供开发者调用的平台

### 技术选型

-   Java Spring Boot
-   MySQL 数据库
-   MyBatis-Plus 及 MyBatis X自动生成
-   API 签名认证
-   Swagger + Knife4j 接口文档生成
-   Spring Cloud Gateway
-   Nacos
-   Open Feign
-   Hutool工具库

### 开始使用

1.   需要先配置启动[Nacos](https://nacos.io/zh-cn/index.html) 项目默认Nacos端口为9527 需要更改Nacos配置or更改项目Nacos端口

2.   「Gateway」微服务中用到了Nacos config 按需更改

     ```java
     /**
      * 在线调用KEY
      */
     @Value("${config.client.key}")
     private String clientKey;
     
     /**
      * 在线调用SECRET
      */
     @Value("${config.client.secret}")
     private String clientSecret;
     ```

     