package com.jungook.zerotodeploy.myInfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyInfoController {
	@GetMapping("/me")
	public String me() {
		return "myInfo";
	}

	@PostMapping("/me")
	public String mePost() {
		return "myInfo";
	};
}
