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
		session.setMaxInactiveInterval(1800);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/info")
	public Map<String, Object> getSessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, Object> response = new HashMap<>();

		if (session != null) {
			long currentTime = System.currentTimeMillis();
			long lastAccessedTime = session.getLastAccessedTime();
			int maxInactiveInterval = session.getMaxInactiveInterval();

			long remainingTime = (lastAccessedTime + (maxInactiveInterval * 1000L) - currentTime) / 1000L;
			remainingTime = Math.max(remainingTime, 0);

			response.put("remainingTime", remainingTime);
		} else {
			response.put("remainingTime", 0);
		}

		return response;
	}
}
