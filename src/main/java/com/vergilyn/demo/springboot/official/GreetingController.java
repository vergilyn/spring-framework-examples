package com.vergilyn.demo.springboot.official;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/1/8
 */
@Controller
//@RestController
public class GreetingController {
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="VergiLyn") String name
            , Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
