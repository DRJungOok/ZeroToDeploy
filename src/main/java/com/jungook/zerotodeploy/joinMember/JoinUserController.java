package com.jungook.zerotodeploy.joinMember;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/join")
public class JoinUserController {
	private final JoinUserService joinUserService;

	@GetMapping
	@Operation(summary = "회원가입 폼 보기")
	public String showJoinUserForm() {
		return "signUp";
	}

	@PostMapping
	@Operation(summary = "회원가입 처리")
	public String handleSignUp(@ModelAttribute JoinUserDTO dto) {
		joinUserService.registerUser(dto);
		return "redirect:/login";
	}
}
