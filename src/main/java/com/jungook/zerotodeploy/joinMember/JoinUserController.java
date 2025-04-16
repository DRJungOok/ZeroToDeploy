package com.jungook.zerotodeploy.joinMember;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/join")
public class JoinUserController {
	private final JoinUserService joinUserService;

	@GetMapping("signUp")
	@Operation(summary = "회원가입 폼 보기")
	public String showJoinUserForm() {
		return "signUp";
	}

	@PostMapping("signUp")
	@Operation(summary = "회원가입 처리")
	public String handleSignUp(@ModelAttribute JoinUserDTO dto, RedirectAttributes redirectAttributes) {
		joinUserService.registerUser(dto);
		redirectAttributes.addFlashAttribute("message", "completes Join User");
		return "redirect:/login";
	}
}
