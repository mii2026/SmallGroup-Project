package com.server.smallgroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SmallgroupApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmallgroupApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("https://10.221.86.77:8080").allowedMethods("GET", "POST", "DELETE").allowedHeaders("*").allowCredentials(true).maxAge(3000);
			}
		};
	}

}
