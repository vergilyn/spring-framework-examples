package com.vergilyn.demo.springboot.db;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vergilyn.demo.springboot.db.dao.JdbcTemplateDao;

@SpringBootApplication
@RestController
public class DatabaseApplication {
	@Autowired
	private JdbcTemplateDao dao;
	public static void main(String[] args) {
		SpringApplication.run(DatabaseApplication.class, args);
	}
	@RequestMapping("/jdbc")
	public String testDao(){
		return dao.testDao()+"";
	}
}
