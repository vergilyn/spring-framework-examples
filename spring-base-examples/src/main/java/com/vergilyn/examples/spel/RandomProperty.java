package com.vergilyn.examples.spel;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:config/spel/value-random.properties")
@Component
@Data
@ToString
public class RandomProperty {

    @Value("${my.secret}")
    private String secret;
    @Value("${my.number}")
    private int number;
    @Value("${my.bignumber}")
    private long bignumber;
    @Value("${my.number.less.than.ten}")
    private int intten;
    @Value("${my.number.in.range}")
    private int range;
}
