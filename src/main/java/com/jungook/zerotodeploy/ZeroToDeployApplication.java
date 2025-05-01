package com.jungook.zerotodeploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.jungook.zerotodeploy")
public class ZeroToDeployApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ZeroToDeployApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ZeroToDeployApplication.class, args);
	}
}
