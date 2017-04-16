package com.vergilyn.demo.springboot.cors.client_a;

import com.vergilyn.demo.springboot.cors.origin.CorsOriginApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/16
 */
@SpringBootApplication
@Controller
public class CorsAApplication implements EmbeddedServletContainerCustomizer {
    private static int CORS_A_PORT = 8081;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CorsAApplication.class);
        app.run(args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(CORS_A_PORT);
    }

    @RequestMapping("corsA")
    public String index(Model model){
        model.addAttribute("port",CORS_A_PORT);
        model.addAttribute("address", CorsOriginApplication.CORS_ADDRESS);
        return "cors/corsA_index";
    }


}
