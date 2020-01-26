# spring-basic-examples

- 自定义注解
- 定时任务
- SpEl(Spring Expression Language)


## SpEl(Spring Expression Language)
spring中@Value的用法：
 1. `${ property : default_value }`
 2. `#{ obj.property ?: default_value }`
 3. (混合，不可逆)`#{ '${}' }`

备注：  
  1. `#`开头的是SpEL(spring表达式语言，Spring Expression Language)，从spring3开始引入。
  2. SpEL 中关于date格式问题该如何解决？

- [Spring @Value 用法小结，#与$的区别](http://www.cnblogs.com/larryzeal/p/5910149.html)