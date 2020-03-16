package com.vergilyn.examples.springboot.cors.client_a;

import com.vergilyn.examples.springboot.cors.origin.CorsOriginApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring Boot2.0以上版本EmbeddedServletContainerCustomizer被WebServerFactoryCustomizer替代
 * @author VergiLyn
 * @date 2017/4/16
 */
@SpringBootApplication
@Controller
public class CorsAApplication {
    private static int CORS_A_PORT = 8081;

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(CORS_A_PORT);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CorsAApplication.class);
        app.run(args);
    }

    @RequestMapping("corsA")
    public String index(Model model){
        model.addAttribute("port",CORS_A_PORT);
        model.addAttribute("address", CorsOriginApplication.CORS_ADDRESS);
        return "cors/corsA_index";
    }
}
