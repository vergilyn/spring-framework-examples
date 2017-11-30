package com.vergilyn.demo.springboot.http.bean;

import java.util.Date;
import java.util.UUID;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
