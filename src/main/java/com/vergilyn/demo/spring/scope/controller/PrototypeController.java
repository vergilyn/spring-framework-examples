package com.vergilyn.demo.spring.scope.controller;

import com.vergilyn.demo.spring.scope.service.PrototypeService;
import com.vergilyn.demo.spring.scope.service.SingletonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/16
 */
@Controller
@Scope("prototype")
public class PrototypeController {
    @Autowired
    private PrototypeService prototypeService;
    private Integer controllerIndex = 1;

    @RequestMapping("/prototype")
    @ResponseBody
    public Map<String, Object> singleton(){
        Map<String, Object> rs = new HashMap<>();
        rs.put("service_index",prototypeService.getIndex());
        rs.put("controller_index",controllerIndex);
        rs.put("controller_hashCode",this.hashCode());
        rs.put("service_hashCode",prototypeService.hashCode());
        rs.put("cache",prototypeService.getCache());
        return rs;
    }
}
