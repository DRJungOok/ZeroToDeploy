package com.jungook.zerotodeploy.joinMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/join")
public class JoinUserController {
	private final JoinUserService joinUserService;

	@GetMapping
	public String showJoinUserForm() {
		return "signUp";
	}

	@PostMapping
	public String handleSignUp(@ModelAttribute JoinUserDTO dto) {
		joinUserService.registerUser(dto);
		return "redirect:/login";
	}
}
