package com.jungook.zerotodeploy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig  implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("https://jung-ook.com")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowCredentials(true);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/templates/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("file:/home/ubuntu/uploads/")
				.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
	}
}
