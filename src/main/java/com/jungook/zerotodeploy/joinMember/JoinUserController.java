package com.jungook.zerotodeploy.joinMember;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class JoinUserController {
	private final JoinUserService joinUserService;

	@GetMapping("signUp")
	@Operation(summary = "회원가입 폼 보기")
	public String showJoinUserForm(Model model) {
		model.addAttribute("joinUserDTO", new JoinUserDTO());
		return "signUp";
	}

	@PostMapping("signUp")
	@Operation(summary = "회원가입 처리")
	public String handleSignUp(@ModelAttribute JoinUserDTO dto, RedirectAttributes redirectAttributes, Model model) {
		try {
			joinUserService.registerUser(dto);
			redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
			return "redirect:/login";
		} catch (RuntimeException e) {
			model.addAttribute("joinUserDTO", dto);
			model.addAttribute("error", e.getMessage());
			return "signUp";
		}
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
