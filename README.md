# SpringBootDemo

2020-01-26:  
  重新整理，spring-boot升级到`2.2.2.RELEASE`，可能导致部分无法运行（特别是牵涉database相关代码）

## 代码结构说明
  1、此项目中是通过spring boot的profile来划分不同功能的application.properties配置。<br>
  2、如果运行有问题，可能是引入了多余的jar，且自动配置失效，又没指定自定义的手动配置。比如，spring-boot-starter-jdbc。

参考文档：<br/>
  《spring-boot-中文参考指南.pdf》<br/>
  [《Spring Boot Reference Guide》](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)<br>

参考github demo：<br/>
 [spring boot mybatis](https://github.com/mybatis/spring-boot-starter.git) <br>
 [spring boot official](https://github.com/spring-projects/spring-boot.git) <br>
 [spring boot samples](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples) <br> 