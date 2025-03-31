package com.jungook.zerotodeploy.login;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final JoinUserRepo joinUserRepo;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/login")
	public String login(@ModelAttribute LoginDTO loginDTO, Model model) {
		JoinUserEntity user = joinUserRepo.findByUserId(loginDTO.getUserId());

		if(user != null && bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
			return "redirect:/";
		}

		model.addAttribute("error", "Invalid username or password");
		return "login";
	}
}
