package com.vergilyn.examples.spel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author vergilyn
 * @date 2020-01-26
 */
@Component
@Slf4j
public class SpelTest {
    @Autowired
    private SpelBasisProperty spelBasisProperty;
    @Autowired
    private SpelProperty spelProperty;
    @Autowired
    private RandomProperty randomProperty;

    public void test(){
        System.out.printf("SpelBasisProperty >>>>  %s \r\n\r\n", spelBasisProperty);
        System.out.printf("SpelProperty >>>>  %s \r\n\r\n", spelProperty);
        System.out.printf("RandomProperty >>>>  %s \r\n\r\n", randomProperty);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RandomProperty.class);

        RandomProperty bean = context.getBean(RandomProperty.class);
        System.out.println(bean);
    }
}
