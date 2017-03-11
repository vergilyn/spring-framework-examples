package com.vergilyn.demo.spring.value;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/* spring中@Value的用法： 
 * 	1、${ property : default_value }
 *  2、#{ obj.property ?: default_value }
 *  3、(混合，不可逆)#{ '${}' }
 * 备注：#开头的是SpEL(spring表达式语言，Spring Expression Language)，从spring3开始引入
 * 参考：http://www.cnblogs.com/larryzeal/p/5910149.html
 */
@SpringBootApplication
public class SpelValueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpelValueApplication.class, args);
	}
}
