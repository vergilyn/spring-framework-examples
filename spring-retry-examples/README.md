# spring-retry-examples

- <https://github.com/spring-projects/spring-retry>

TODO: [Try again base on the result not just exception](https://github.com/spring-projects/spring-retry/issues/231)
貌似无法根据“方法执行结果”进行重试（只支持根据 异常重试，源码所在`org.springframework.retry.support.RetryTemplate#doExecute(...)`），
例如 发送短信，第三方接口调用成功，但返回 error-code，
针对某些特定 error-code（例如 触发第三方接口限流），允许间隔多久重试。

2022-05-07 >>>>  
[GH-231: Support Retry Based on Result](https://github.com/spring-projects/spring-retry/pull/289)
