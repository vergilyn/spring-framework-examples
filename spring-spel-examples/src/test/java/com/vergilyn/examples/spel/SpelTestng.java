package com.vergilyn.examples.spel;

import com.vergilyn.examples.spel.properties.BaseProperties;
import com.vergilyn.examples.spel.properties.CollectionsProperties;
import com.vergilyn.examples.spel.properties.ReferenceBeanProperties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.testng.annotations.Test;

/**
 * @author vergilyn
 * @date 2020-04-20
 */
public class SpelTestng {
    private AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    private ExpressionParser parser = new SpelExpressionParser();
    private EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

    @Test
    public void base(){
        applicationContext.register(BaseProperties.class);
        applicationContext.refresh();

        BaseProperties base = applicationContext.getBean(BaseProperties.class);
        System.out.println(base);
    }

    @Test
    public void referenceBean(){
        applicationContext.register(BaseProperties.class, ReferenceBeanProperties.class);
        applicationContext.refresh();

        ReferenceBeanProperties bean = applicationContext.getBean(ReferenceBeanProperties.class);
        System.out.println(bean);
    }

    /**
     * SpEL 对与类型 array, list, map 在 properties文件中的<strong>书写方式及其不友好</strong>！
     */
    @Test
    public void collections(){
        applicationContext.register(CollectionsProperties.class);
        applicationContext.refresh();

        CollectionsProperties bean = applicationContext.getBean(CollectionsProperties.class);
        System.out.println(bean);
    }
}
