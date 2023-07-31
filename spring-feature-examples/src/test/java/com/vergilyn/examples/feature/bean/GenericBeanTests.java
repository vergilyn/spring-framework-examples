package com.vergilyn.examples.feature.bean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericBeanTests {

    @Test
    void testFindGenericBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SmsMessageConverter.class, EmailMessageConverter.class);
        context.refresh();

        Map<String, MessageConverter> beansOfType = context.getBeansOfType(MessageConverter.class);
        assertThat(beansOfType).hasSize(2);

        // org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.vergilyn.examples.feature.bean.GenericBeanTests$MessageConverter' available:
        //   expected single matching bean but found 2: genericBeanTests.SmsMessageConverter,genericBeanTests.EmailMessageConverter
        // SmsMessageConverter smsMessageConverter = (SmsMessageConverter) context.getBean(MessageConverter.class);

        // 无法通过`context`直接拿到 bean。只能先拿到 bean-name，然后再通过bean-name获取 bean。然后再类型强转！
        String[] beanNamesForType = context.getBeanNamesForType(ResolvableType.forType(new ParameterizedTypeReference<MessageConverter<Sms>>() {}));
        assertThat(beanNamesForType).hasSize(1);

        Throwable throwable = Assertions.catchThrowable(() -> {
            MessageConverter<Sms> bean = (MessageConverter<Sms>) context.getBean(beanNamesForType[0]);
            SmsMessageConverter smsMessageConverter = (SmsMessageConverter) context.getBean(beanNamesForType[0]);
        });

        assertThat(throwable).isNull();

    }

    private static interface MessageConverter<T> {
        T convert(Object source);
    }

    private static class SmsMessageConverter implements MessageConverter<Sms> {

        @Override
        public Sms convert(Object source) {
            return new Sms();
        }
    }

    private static class EmailMessageConverter implements MessageConverter<Email> {

        @Override
        public Email convert(Object source) {
            return new Email();
        }
    }

    private static class Sms {
        private String name = "SMS";
    }

    private static class Email {
        private String name = "Email";
    }
}
