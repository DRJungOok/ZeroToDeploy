package com.jungook.zerotodeploy.user;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	@GetMapping("/signup")
	public String signup(UserCreateForm form) {
		return "signUp";
	}
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signUp";
		}

		if(!form.getPassword1().equals(form.getPassword2())) {
			bindingResult.rejectValue("password2", "Passwords do not match");
			return "signUp";
		}

		userService.create(form.getUsername(), form.getEmail(), form.getPassword1());
		return "redirect:signup";
	}
}