package com.vergilyn.demo.springboot.cors.origin;

import com.vergilyn.demo.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/16
 */
@Controller
@RequestMapping("/cors")
public class CorsOriginController {

    /**
     * 如果CrosFilter中配置有Access-Control-Allow-Origin, 则CrossOrigin无效。
     * 否则, 以@CrossOrigin为准。
     * @return
     */
    @CrossOrigin(origins = "http://127.0.0.1:8082")
    @RequestMapping("/get")
    @ResponseBody
    public Map<String,Object> get(){
        return Constant.map;
    }

}
