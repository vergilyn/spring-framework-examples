# 【002】spring - BeanWrapper & BeanDefinition.MD

- [Spring IOC BeanWrapper](https://blog.csdn.net/u012410733/article/details/53346345)

> BeanDefinition提供数据，然后BeanWrapper负责依赖注入


+ <https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#beans-child-bean-definitions>


## case - 01
dubbo service-provider 创建 invoker 时会依赖`ServiceBean -> protected T ref`，"The reference of the interface implementation"。  

**FAQ.** `ref`是何时设置到对象中的？
