package com.vergilyn.demo.spring.scope.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/16
 */
@Service
@Scope("prototype")
public class PrototypeService {
    private Map<String,Object> cache = new HashMap<>();
    private Integer index = 1;

    public Map<String, Object> getCache() {
        return cache;
    }

    public Integer getIndex() {
        cache.put("index-"+index,index);
        return index++;
    }
}
