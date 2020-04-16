# 【002】spring - BeanWrapper.MD

- [Spring IOC BeanWrapper
](https://blog.csdn.net/u012410733/article/details/53346345)


## case - 01
dubbo service-provider 创建 invoker 时会依赖`ServiceBean -> protected T ref`，"The reference of the interface implementation"。  

**FAQ.** `ref`是何时设置到对象中的？
