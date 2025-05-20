package com.jungook.zerotodeploy.myInfo;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myInfo")
public class MyInfoController {
	private final JoinUserRepo joinUserRepo;

	public MyInfoController(JoinUserRepo joinUserRepo) {
		this.joinUserRepo = joinUserRepo;
	}

	@GetMapping
	public String myInfo(Model model, Authentication authentication) {
		String userName = authentication.getName();
		JoinUserEntity user = joinUserRepo.findByUserName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("not found user: " + userName));

		model.addAttribute("user", user);
		return "myInfo";
	}

	@PostMapping("/uploadProfile")
	public String mePost() {

		return "myInfo";
	};
}
