package com.vergilyn.demo.bean.database;

import java.io.Serializable;

/** 对应数据库的bean
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
public class Parent implements Serializable{
    private int parentId;
    private String parentName;
    public Parent() {
    }

    public Parent(int parentId, String parentName) {
        this.parentId = parentId;
        this.parentName = parentName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
