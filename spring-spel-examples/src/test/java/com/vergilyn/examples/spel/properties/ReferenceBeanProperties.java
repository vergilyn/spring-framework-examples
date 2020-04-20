package com.vergilyn.examples.spel.properties;

import java.time.LocalTime;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author vergilyn
 * @date 2020-04-20
 */
@PropertySource("classpath:spel/reference.properties")
@Component
@Data
public class ReferenceBeanProperties extends DefaultToString {

    /**
     * 等价于 {@linkplain BaseProperties#name}
     */
    @Value("#{base.name}")
    private String baseName;

    /**
     * 解析：由内往外，${spel.mix} = "name"。然后在spel表达式中，('')表示定义字符串。
     * 所以 #{'name'} = "name"
     */
    @Value("#{ '${vergilyn.spel.reference.mix}' }")
    private String mix;

    //组合，特别.后面跟的是对象属性。所以要是class中的属性，而不是properties中的key
    @Value("#{base. ${vergilyn.spel.reference.mix} }")
    private String mixName;

    /**
     * `base` -> {@linkplain BaseProperties}。
     * 在SpEL中避免抛出空指针异常（NullPointException）的方法是使用null-safe存取器：
     * ?. 运算符代替点（.） #{object?.method()},如果object=null，则不会调用method()
     */
    @Value("#{base.getRef()}")
    public String baseRef;

    @Value("#{base.getLocalTime()}")	//可以是任何类型
    public LocalTime baseLocalTime;

    @Value("#{null?.toString()}")	//当?.左边为null时，不再执行右边的方法。
    public String baseNull;

    @Override
    public String toString() {
        return super.toString();
    }
}
