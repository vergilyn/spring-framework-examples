# spring-boot-basic-examples

## 说明
1. 此项目中是通过spring boot的profile来划分不同功能的application.properties配置。
2. 如果运行有问题，可能是引入了多余的jar，且自动配置失效，又没指定自定义的手动配置。比如，spring-boot-starter-jdbc。

参考文档：  
- 《spring-boot-中文参考指南.pdf》
+ [《Spring Boot Reference Guide》](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

参考github demo：  
+ [spring boot official](https://github.com/spring-projects/spring-boot.git) <br>
+ [spring boot samples](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples) <br>
+ [spring boot mybatis](https://github.com/mybatis/spring-boot-starter.git) <br>

2020-01-26:  
  重新整理，spring-boot升级到`2.2.2.RELEASE`，可能导致部分无法运行（特别是牵涉database相关代码）
  
  
## examples
- aop
- banner
- cacheable: spring-cache，可以和redis、jcache、java-memory等结合
- cros
- **interceptor:** <font color="red">spring-boot-2.x中因为依赖的是spring 5.x，会导致静态资源也被拦截！</font>
- official: 官方示例
- profiles
- properties: 自定义配置文件`*.properties` 或 `*.yaml`。
- **http:** 特殊的网络文件下载示例

## interceptor
1. (个人觉得) 拦截器能实现filter的功能，反之则不一定。那么适用性怎么抉择，或者说interceptor、filter实质的使用/应用区别是什么?

2. interceptor怎么在preHandle(...)中修改传到controller的参数值?

3. preHandle(...)拦截调后怎么处理转发/重定向?感觉demo的写法不好。比如检测未登陆，转发/重定向到登陆页面。

4. interceptor怎么在postHandle中得到controller返回的自定义对象结果?（比如Map、List等，被@ResponseBody注解）

5. interceptor的注入方式，不喜欢demo中的InterceptorConfiguration注入方式，是否还有别的bean注入? 如filter的@WebFilter。


## properties VS yaml
23.6.4. YAML缺点  
  YAML文件不能通过@PropertySource注解加载。所以，在这种情况下，如果需要使用@PropertySource注解的方式加载值，那就要使用properties文件。

## 遇到的问题
### spring-boot-v2.x 自定义拦截器`HandlerInterceptor`后导致静态资源无法访问

> [spring boot 2.x静态资源会被HandlerInterceptor拦截的原因和解决方法](https://my.oschina.net/dengfuwei/blog/1795346)
> 因为spring boot 2.x依赖的spring 5.x版本，相对于spring boot 1.5.x依赖的spring 4.3.x版本而言，
> 针对资源的拦截器初始化时有区别，具体源码在WebMvcConfigurationSupport中

### `@Cacheable` 如何设置有效时间？
- [优雅的缓存解决方案--设置过期时间](https://blog.csdn.net/chachapaofan/article/details/88930778)

### CROS， Spring Boot2.0 `EmbeddedServletContainerCustomizer`被`WebServerFactoryCustomizer`
> [聊聊springboot2的embeded container的配置改动](https://segmentfault.com/a/1190000014610478)
> Spring Boot2.0以上版本EmbeddedServletContainerCustomizer被WebServerFactoryCustomizer替代

### filter
1. 过滤器filter  
对于实现过滤器的2种方式：
  1) 基于servlet的，ServletFilter implements Filter，类注解@WebFilter。（javax.servlet.Filter）

  2) 基于spring mvc的，SpringMvcFilter extends OncePerRequestFilter，类注解@Component(或者其他bean注入方式)。
    （org.springframework.web.filter.OncePerRequestFilter）
	除开OncePerRequestFilter，spring mvc中还定义了很多别的filter，参考spiring-web.jar中org.springframework.web.filter。
	当然，它们全都implements Filter。
	（参考：http://blog.csdn.net/ochangwen/article/details/52727743）

**区别：**
  1) 不管用@Component注解ServletFilter或SpringMvcFilter，在application中都无需注解@ServletComponentScan，且filter正常可用。
	  但有一个问题是：怎么指定需要过滤的路径？

  2) 不管是implements Filter还是extends OncePerRequestFilter，只要注解为@WebFilter，那么application必须要@ServletComponentScan，
	  不然filter不会被正确的注入（即无效的filter）

**特别：**
1. 修改请求参数数值
 因为HttpServletRequest.getParameterMap()无法被改变，所以需要extends HttpServletRequestWrapper并重写部分方法。
 参考： https://my.oschina.net/alexnine/blog/730706

2. 过滤器、拦截器等的执行顺序
 仔细理解：https://my.oschina.net/ydsakyclguozi/blog/413789