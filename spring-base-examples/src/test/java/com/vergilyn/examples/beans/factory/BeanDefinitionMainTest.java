package com.vergilyn.examples.beans.factory;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import static com.vergilyn.examples.beans.factory.AbstractService.FIRST_BEAN_NAME;
import static com.vergilyn.examples.beans.factory.AbstractService.SECOND_BEAN_NAME;

/**
 * 结合 dubbo源码 和 以下代码，悟！
 * @author vergilyn
 * @date 2020-04-16
 */
public class BeanDefinitionMainTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FirstService.class, SecondService.class);
        ConfigurableEnvironment environment = context.getEnvironment();

        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(FirstService.class);
        definitionBuilder.addPropertyReference("ref", SECOND_BEAN_NAME);
        definitionBuilder.addPropertyValue("number", 10086);

        context.registerBeanDefinition(FIRST_BEAN_NAME, definitionBuilder.getBeanDefinition());

        FirstService firstService = (FirstService) context.getBean(FIRST_BEAN_NAME);
        firstService.print();

        SecondService secondService = (SecondService) context.getBean(SECOND_BEAN_NAME);
        System.out.printf("validation singleton >>>> %b \r\n", secondService == firstService.getRef());
    }
}

abstract class AbstractService {
    protected static final String FIRST_BEAN_NAME = "firstService";
    protected static final String SECOND_BEAN_NAME = "secondService";

    private AbstractService ref;

    public abstract Integer getNumber();

    public void print(){
        System.out.printf("%s >>>> number: %d, ref: %s, ref.number: %d \r\n",
                this.getClass().getSimpleName(),
                getNumber(),
                getRef().getClass().getSimpleName(),
                ref.getNumber());
    }

    public AbstractService getRef() {
        return ref;
    }

    public void setRef(AbstractService ref) {
        this.ref = ref;
    }
}

@Component(AbstractService.FIRST_BEAN_NAME)
class FirstService extends AbstractService {
    private Integer number = 1;

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public Integer getNumber() {
        return this.number;
    }
}

@Component(SECOND_BEAN_NAME)
class SecondService extends AbstractService {
    private Integer number = 2;

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public Integer getNumber() {
        return this.number;
    }
}
