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
	public ResponseEntity<?> extendSession(HttpSession session) {
		session.setMaxInactiveInterval(3600);
		session.setAttribute("loginTimestamp", System.currentTimeMillis());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/info")
	public Map<String, Object> getSessionInfo(HttpServletRequest request) {
		boolean isLoggedIn = request.getUserPrincipal() != null;
		HttpSession session = request.getSession(false);
		Map<String, Object> response = new HashMap<>();

		response.put("isLoggedIn", isLoggedIn);

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
			response.put("isLoggedIn", true);
		} else {
			response.put("remainingTime", 0);
			response.put("isLoggedIn", false);
		}

		return response;
	}
}
