package com.jungook.zerotodeploy.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Configuration
public class GlobalPathAdvice {

	@ModelAttribute("currentPath")
	public String setCurrentPath(HttpServletRequest request) {
		return request.getRequestURI();
	}
}