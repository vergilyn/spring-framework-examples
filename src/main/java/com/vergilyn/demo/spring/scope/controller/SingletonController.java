package com.vergilyn.demo.spring.scope.controller;

import com.vergilyn.demo.spring.scope.service.SingletonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/16
 */
@Controller
@Scope("singleton")
public class SingletonController {
    @Autowired
    private SingletonService singletonService;
    private Integer controllerIndex = 1;

    @RequestMapping("/singleton")
    @ResponseBody
    public Map<String, Object> singleton(){
        Map<String, Object> rs = new HashMap<>();
        rs.put("service_index",singletonService.getIndex());
        rs.put("controller_index",controllerIndex);
        rs.put("controller_hashCode",this.hashCode());
        rs.put("service_hashCode",singletonService.hashCode());
        rs.put("cache",singletonService.getCache());
        return rs;
    }
}
