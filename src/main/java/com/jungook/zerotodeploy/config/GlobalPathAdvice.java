package com.jungook.zerotodeploy.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalPathAdvice {

	@ModelAttribute("currentPath")
	public String setCurrentPath(HttpServletRequest request) {
		return request.getRequestURI();
	}
}