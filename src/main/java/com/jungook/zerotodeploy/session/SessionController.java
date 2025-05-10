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

	@GetMapping("/info")
	public Map<String, Object> getSessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, Object> response = new HashMap<>();
		if (session != null) {
			long now = System.currentTimeMillis();
			long expireAt = session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L;
			response.put("remainingTime", (expireAt - now) / 1000);
		} else {
			response.put("remainingTime", 0);
		}
		return response;
	}



	@PostMapping("/extend")
	public ResponseEntity<String> extendSession(HttpServletRequest request) {
		request.getSession(true);
		return ResponseEntity.ok("Session extended");
	}
}
