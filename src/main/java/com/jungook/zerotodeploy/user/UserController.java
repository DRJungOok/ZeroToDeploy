package com.jungook.zerotodeploy.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("form", new UserCreateForm());
		return "signUp";
	}

	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", form);
			return "signUp";
		}

		if (!form.getPassword1().equals(form.getPassword2())) {
			bindingResult.rejectValue("password2", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("form", form);
			return "signUp";
		}

		userService.create(form.getUsername(), form.getEmail(), form.getPassword1());
		return "redirect:/signup";
	}
}
