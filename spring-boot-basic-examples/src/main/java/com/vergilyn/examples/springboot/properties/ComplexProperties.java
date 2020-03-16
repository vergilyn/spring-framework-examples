package com.vergilyn.examples.springboot.properties;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vergilyn.complex")
@Data
@ToString
public class ComplexProperties {
    private Map<String, Object> map;
    private List<String> list;
    private String[] array;
}
