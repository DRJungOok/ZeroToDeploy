package com.jungook.zerotodeploy.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

	@PostMapping("/extend")
	public ResponseEntity<?> extendSession(HttpServletRequest request) {
		// 로그인하지 않은 사용자는 세션 연장 불가
		if (request.getUserPrincipal() == null) {
			return ResponseEntity.status(401).build();
		}
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.setMaxInactiveInterval(3600);
			session.setAttribute("loginTimestamp", System.currentTimeMillis());
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/info")
	public Map<String, Object> getSessionInfo(HttpServletRequest request) {
		boolean isLoggedIn = request.getUserPrincipal() != null;
		Map<String, Object> response = new HashMap<>();

		response.put("isLoggedIn", isLoggedIn);

		// 로그인하지 않은 사용자에게는 세션 정보를 제공하지 않음
		if (!isLoggedIn) {
			response.put("remainingTime", 0);
			return response;
		}

		// 로그인한 사용자만 세션 정보 조회
		HttpSession session = request.getSession(false);
		if (session != null) {
			Long loginTimestamp = (Long) session.getAttribute("loginTimestamp");
			if (loginTimestamp == null) {
				loginTimestamp = System.currentTimeMillis();
				session.setAttribute("loginTimestamp", loginTimestamp);
			}

			long fixedTimeoutMillis = 60 * 60 * 1000L;
			long now = System.currentTimeMillis();
			long remainingTime = (loginTimestamp + fixedTimeoutMillis - now) / 1000L;
			remainingTime = Math.max(remainingTime, 0);

			response.put("remainingTime", remainingTime);
		} else {
			response.put("remainingTime", 0);
		}

		return response;
	}
}
