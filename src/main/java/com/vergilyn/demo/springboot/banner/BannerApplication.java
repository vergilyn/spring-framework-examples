package com.vergilyn.demo.springboot.banner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@PropertySource(value = { "classpath:config/banner/banner.properties" },ignoreResourceNotFound=false)
@Controller
public class BannerApplication {
	@Value("${flag}")
	private String flag;
	@Value("${banner.location}")
	private String bannerPath;

	public static void main(String[] args) {
//		SpringApplication.run(CustomBanner.class, args);
		SpringApplication app = new SpringApplication(BannerApplication.class);
//		app.setEnvironment(environment);
//		app.setBannerMode(Banner.Mode.OFF); //设置banner
//		app.setAddCommandLineProperties(false);	//禁用命令行参数
		app.run(args);
	}

	@RequestMapping("/banner")
	public String banner(
			@RequestParam(value = "name", required = false, defaultValue = "VergiLyn") String name,
			Model model) {
		model.addAttribute("name", name);
		System.out.println(flag);
		System.out.println(bannerPath);
		return "greeting";
	}
}
