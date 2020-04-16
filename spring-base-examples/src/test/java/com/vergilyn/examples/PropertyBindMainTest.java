package com.vergilyn.examples;

import java.util.Map;

import com.beust.jcommander.internal.Maps;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

/**
 * @author VergiLyn
 * @date 2020-03-16
 */
public class PropertyBindMainTest {

    public static void main(String[] args) {
        ComplexBean bean = new ComplexBean();

        DataBinder dataBinder = new DataBinder(bean);
        // Set ignored*
        dataBinder.setIgnoreInvalidFields(true);
        dataBinder.setIgnoreUnknownFields(true);

        Map<String, Object> map = Maps.newHashMap();
        map.put("id", 10086L);
        map.put("name", "vergilyn");

        // spring-beans v5.2.2.RELEASE 基于以下语法无法成功绑定
        // 类似 *.properties 语法 `maps.item1=value1`
        map.put("maps.item1", "value1");
        map.put("maps", "[{item3:value3}, {item4:value4}]");

        // spring-beans v5.2.2.RELEASE 可以成功绑定
        // 类似 *.properties 语法 `maps[item5]=value5`
        map.put("maps[item5]", "value5");

        // Get properties under specified prefix from PropertySources
        // Convert Map to MutablePropertyValues
        MutablePropertyValues propertyValues = new MutablePropertyValues(map);
        // Bind
        dataBinder.bind(propertyValues);


        System.out.println(bean);
    }

    @NoArgsConstructor
    @Data
    @ToString
    static class ComplexBean {
        private Long id;
        private String name;
        private Map<String, String> maps;
    }
}
