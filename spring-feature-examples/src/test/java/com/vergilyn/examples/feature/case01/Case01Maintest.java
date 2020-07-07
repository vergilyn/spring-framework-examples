package com.vergilyn.examples.feature.case01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * {@linkplain Case01Configuration} >>>>
 * <pre>
 * ComponentScan:
 * ```
 * B >>>> 1
 * B >>>> 2
 * A >>>> 3
 * ```
 *
 * Configuration:
 * ```
 * B >>>> 1
 * A >>>> 2
 * ```
 * </pre>
 *
 * @author vergilyn
 * @date 2020-07-07
 *
 * @see <a href="https://mp.weixin.qq.com/s/QtQza9JW93kwcAzuS5SUYQ">配置类为什么要添加@Configuration注解？</a>
 */
public class Case01Maintest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Case01Configuration.class);
    }
}
