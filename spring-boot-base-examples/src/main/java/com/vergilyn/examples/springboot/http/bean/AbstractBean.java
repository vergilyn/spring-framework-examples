package com.vergilyn.examples.springboot.http.bean;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.ToString;

/**
 * @author VergiLyn
 * @date 2017/11/18
 */
@Data
@ToString
public abstract class AbstractBean {
    private String id;
    private Date createTime;
    private Date updateTime;
    private boolean isDeleted;

    public AbstractBean() {
        this.id = UUID.randomUUID().toString();
        Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
        this.isDeleted = false;
    }
}
