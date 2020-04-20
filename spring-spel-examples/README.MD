# spring-spel-examples

+ [SpEL, 5.2.5.RELEASE](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#expressions)


The expression language supports the following functionality:  
- Literal expressions
- Boolean and relational operators
- Regular expressions
- Class expressions
- Accessing properties, arrays, lists, and maps
- Method invocation
- Relational operators
- Assignment
- Calling constructors
- Bean references
- Array construction
- Inline lists
- Inline maps
- Ternary operator
- Variables
- User-defined functions
- Collection projection
- Collection selection
- Templated expressions

[expressions-language-ref, 5.2.5.RELEASE](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#expressions-language-ref)


## SpEl(Spring Expression Language)
spring中@Value的用法：
 1. `${ property : default_value }`
 2. `#{ obj.property ?: default_value }`
 3. (混合，不可逆)`#{ '${}' }`

备注：  
  1. `#`开头的是SpEL(spring表达式语言，Spring Expression Language)，从spring3开始引入。
  2. SpEL 中关于date格式问题该如何解决？

- [Spring @Value 用法小结，#与$的区别](http://www.cnblogs.com/larryzeal/p/5910149.html)