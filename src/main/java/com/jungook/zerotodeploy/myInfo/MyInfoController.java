package com.jungook.zerotodeploy.myInfo;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
		if (authentication == null) {
			return "redirect:/login";
		}

		String loginId = authentication.getName();
		JoinUserEntity user = joinUserRepo.findByUserName(loginId)
				.or(() -> joinUserRepo.findByEmail(loginId))
				.orElseThrow(() -> new UsernameNotFoundException("not found user: " + loginId));

		model.addAttribute("user", user);
		model.addAttribute("isEditable", true);
		return "myInfo";
	}

	@GetMapping("/{userNameOrEmail}")
	public String userInfo(@PathVariable String userNameOrEmail, Model model) {
		JoinUserEntity user = joinUserRepo.findByUserName(userNameOrEmail)
				.or(() -> joinUserRepo.findByEmail(userNameOrEmail))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		model.addAttribute("user", user);
		model.addAttribute("isEditable", false);
		return "myInfo";
	}

	@PostMapping("/uploadProfile")
	public String uploadProfile(Model model, Authentication authentication,
								@RequestParam("profileImage") MultipartFile file) throws IOException {
		if (authentication == null) return "redirect:/login";

		String loginId = authentication.getName();
		JoinUserEntity user = joinUserRepo.findByUserName(loginId)
				.or(() -> joinUserRepo.findByEmail(loginId))
				.orElseThrow(() -> new UsernameNotFoundException("not found user: " + loginId));

		if (!file.isEmpty()) {
			String uploadDir = getUploadDir();
			String originalFileName = file.getOriginalFilename();
			String cleanedFileName = originalFileName.replaceAll("\\s+", "_");
			String newFileName = UUID.randomUUID() + "_" + cleanedFileName;

			Path uploadPath = Paths.get(uploadDir, newFileName).normalize();
			Files.createDirectories(uploadPath.getParent());
			Files.write(uploadPath, file.getBytes());

			user.setProfileImage(newFileName);
			joinUserRepo.save(user);
		}

		model.addAttribute("user", user);
		return "redirect:/myInfo";
	}

	@PostMapping("/{id}/update")
	public String updateInfo(@PathVariable Long id,
							 @RequestParam("userName") String newUserName,
							 @RequestParam("email") String newEmail,
							 @RequestParam(value = "password", required = false) String newPassword,
							 Authentication authentication,
							 HttpServletRequest request) {

		JoinUserEntity user = joinUserRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		boolean isSelf = authentication != null &&
				(authentication.getName().equals(user.getUserName()) ||
						authentication.getName().equals(user.getEmail()));
		if (!isSelf) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

		user.setUserName(newUserName);
		user.setEmail(newEmail);
		if (newPassword != null && !newPassword.isBlank()) {
			user.setPassword(passwordEncoder.encode(newPassword));
		}

		joinUserRepo.save(user);

		// SecurityContext 재설정
		User updatedUserDetails = new User(user.getUserName(), user.getPassword(), authentication.getAuthorities());
		Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
				updatedUserDetails, authentication.getCredentials(), authentication.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		request.getSession().setAttribute(
				org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());

		String encodedUserName = URLEncoder.encode(user.getUserName(), StandardCharsets.UTF_8);
		return "redirect:/myInfo/" + encodedUserName;
	}

	public String getUploadDir() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "C:/uploads/";
		} else if (os.contains("mac")) {
			return "/Users/uploads/";
		} else {
			return "/home/ubuntu/uploads/";
		}
	}
}
