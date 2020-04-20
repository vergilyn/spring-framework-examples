package com.vergilyn.examples.spel.properties;

import java.util.List;
import java.util.Map;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * <a href="https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#expressions-properties-arrays">expressions-properties-arrays</a>
 * @author vergilyn
 * @date 2020-04-20
 */
@PropertySource("classpath:spel/collections.properties")  // 不支持 yaml
@Component
@Data
public class CollectionsProperties extends DefaultToString {
    @Value("${vergilyn.spel.collections.arrays}")
    private String[] arrays;

    @Value("#{ '${vergilyn.spel.collections.list}'.split(',')}")
    private List<String> list;

    @Value("#{ ${vergilyn.spel.collections.map} }")
    private Map<String, String> map;

    @Override
    public String toString() {
        return super.toString();
    }
}
