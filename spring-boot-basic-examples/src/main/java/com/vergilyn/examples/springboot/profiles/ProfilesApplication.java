package com.vergilyn.examples.springboot.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * {@linkplain org.springframework.context.annotation.Profile} 可以实现不同环境下配置参数的切换，任何@Component或@Configuration注解的类都可以使用@Profile注解。
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
public class ProfilesApplication implements CommandLineRunner {
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProfilesApplication.class);
        app.setAdditionalProfiles("profiles", "prod");	//代码指定
        app.run(args);
    }

    @Profile("dev")
    private void dev(){
        System.out.println("profile dev >>>> " + environment.getProperty("spring.application.name"));
    }

    @Profile("prod")
    private void prod(){
        System.out.println("profile prod >>>> " + environment.getProperty("spring.application.name"));
    }

    @Override
    public void run(String... args) throws Exception {
        dev();

        prod();
    }
}
