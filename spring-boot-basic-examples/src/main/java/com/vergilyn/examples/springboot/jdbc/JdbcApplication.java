package com.vergilyn.examples.springboot.jdbc;

import java.util.HashMap;
import java.util.Map;

import com.vergilyn.examples.springboot.properties.PropertiesApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Spring的JdbcTemplate和NamedParameterJdbcTemplate类是被自动配置的，
 * 你可以在自己的beans中通过@Autowire直接注入它们。
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
public class JdbcApplication implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PropertiesApplication.class);
        application.setAdditionalProfiles("jdbc");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        testDao();
    }

    @SuppressWarnings("unused")
    public Long testDao(){
        Map<String,Object> param = new HashMap<String,Object>();
        String sql = " select count(*) from child where child_name = :name";
        param.put("name", "周一");

        Long count = this.namedJdbc.queryForObject(sql, param, Long.class);
        System.out.println(count);
        return count;
    }
}
