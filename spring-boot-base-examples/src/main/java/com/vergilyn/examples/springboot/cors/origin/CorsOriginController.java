package com.vergilyn.examples.springboot.cors.origin;

import java.util.Map;

import com.google.common.collect.Maps;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/16
 */
@Controller
@RequestMapping("/cors")
public class CorsOriginController {

    public static final Map<String, Object> map = Maps.newHashMap();

    static {
        map.put("name", "vergilyn");
        map.put("email", "vergilyn@vip.qq.com");
    }

    /**
     * 如果CrosFilter中配置有Access-Control-Allow-Origin, 则CrossOrigin无效。
     * 否则, 以@CrossOrigin为准。
     * @return
     */
    @CrossOrigin(origins = "http://127.0.0.1:8082")
    @RequestMapping("/get")
    @ResponseBody
    public Map<String,Object> get(){
        return map;
    }

}
