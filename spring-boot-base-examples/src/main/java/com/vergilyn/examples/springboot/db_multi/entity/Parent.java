package com.vergilyn.examples.springboot.db_multi.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Parent implements Serializable{
    private int parentId;
    private String parentName;

    public Parent(int parentId, String parentName) {
        this.parentId = parentId;
        this.parentName = parentName;
    }

}
