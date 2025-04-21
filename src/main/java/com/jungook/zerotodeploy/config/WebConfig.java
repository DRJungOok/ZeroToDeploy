package com.jungook.zerotodeploy.config;

import jakarta.annotation.Nonnull;
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
		String os = System.getProperty("os.name").toLowerCase();
		String uploadPath;

		if (os.contains("mac")) {
			// macOS
			uploadPath = "file:/Users/kimjungook/uploads/";
		} else if (os.contains("win")) {
			// Windows
			uploadPath = "file:///C:/uploads/";
		} else {
			// Linux (Ubuntu)
			uploadPath = "file:/home/ubuntu/uploads/";
		}

		registry.addResourceHandler("/uploads/**")
				.addResourceLocations(uploadPath)
				.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));

		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/templates/", "classpath:/static/");
	}

}
