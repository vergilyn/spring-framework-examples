package com.vergilyn.demo.spring.scope.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/16
 */
@Controller
public class ScopeController {
    @RequestMapping("/scope")
    public String scopeIndex(){
        return "scope/scope_index";
    }
}
