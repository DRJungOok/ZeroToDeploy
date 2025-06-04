package com.jungook.zerotodeploy.myInfo;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/myInfo")
public class MyInfoController {
	private final JoinUserRepo joinUserRepo;
	private final BCryptPasswordEncoder passwordEncoder;

	public MyInfoController(JoinUserRepo joinUserRepo, BCryptPasswordEncoder passwordEncoder) {
		this.joinUserRepo = joinUserRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public String myInfo(Model model, Authentication authentication) {
		if(authentication == null) {
			return "redirect:/login";
		}

		String userName = authentication.getName();
		JoinUserEntity user = joinUserRepo.findByUserName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("not found user: " + userName));

                model.addAttribute("user", user);
                model.addAttribute("isEditable", true);
                return "myInfo";
	}

	@PostMapping("/uploadProfile")
	public String uploadProfile(Model model, Authentication authentication, @RequestParam("profileImage")MultipartFile file) throws IOException {
		String userName = authentication.getName();
		JoinUserEntity user = joinUserRepo.findByUserName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("not found user: " + userName));

		if(!file.isEmpty()) {
			String uploadDir = getUploadDir();
			String fileName = file.getOriginalFilename();
			Path path = Paths.get(uploadDir, fileName).normalize();
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());

			user.setProfileImage(fileName);
			joinUserRepo.save(user);
		}
		model.addAttribute("user", user);
		return "myInfo";
	};

	public String getUploadDir() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "C:/uploads/";
		} else if (os.contains("mac")) {
			return "Users/uploads/";
		} else {
			return "/home/ubuntu/uploads/";
		}
	}

	@PostMapping("/update")
	public String updateInfo(Model model, Authentication authentication,
							 @RequestParam("userName") String newUserName,
							 @RequestParam("email") String newEmail,
							 @RequestParam(value = "password", required = false) String newPassword) throws IOException {
		String userName = authentication.getName();
		JoinUserEntity user = joinUserRepo.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("not found user: " + userName));

		user.setUserName(newUserName);
		user.setEmail(newEmail);

		if(newPassword != null && !newPassword.isEmpty()) {
			String encodedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);
		}

		joinUserRepo.save(user);
		model.addAttribute("user", user);
		return "redirect:/myInfo";
	}
}
