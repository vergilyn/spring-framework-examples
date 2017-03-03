package com.vergilyn.demo.springboot.properties.bean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/2/25
 */
@Configuration
@ConfigurationProperties()
@PropertySource("classpath:config/properties/RandomProperty.properties")
@Component
public class RandomPropertyBean {

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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getBignumber() {
        return bignumber;
    }

    public void setBignumber(long bignumber) {
        this.bignumber = bignumber;
    }

    public int getIntten() {
        return intten;
    }

    public void setIntten(int intten) {
        this.intten = intten;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
