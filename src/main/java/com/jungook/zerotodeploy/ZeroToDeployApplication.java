package com.jungook.zerotodeploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.jungook.zerotodeploy")
public class ZeroToDeployApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeroToDeployApplication.class, args);
	}

}
