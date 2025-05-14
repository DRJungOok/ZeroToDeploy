package com.jungook.zerotodeploy.oauth;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Getter
public class OAuthAttributes {
	private static final Logger log = LoggerFactory.getLogger(OAuthAttributes.class);

	private final String name;
	private final String email;
	private final String provider;

	public OAuthAttributes(String name, String email, String provider) {
		this.name = name;
		this.email = email;
		this.provider = provider;
	}

	public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
		log.info("📍 OAuthAttributes.of 호출됨 - registrationId: {}", registrationId);

		return switch (registrationId) {
			case "naver" -> ofNaver(attributes);
			case "kakao" -> ofKakao(attributes);
			default -> {
				log.warn("⚠️ 알 수 없는 registrationId: {} - Google 처리로 fallback", registrationId);
				yield ofGoogle(attributes);
			}
		};
	}

	private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
		return new OAuthAttributes(
				(String) attributes.get("name"),
				(String) attributes.get("email"),
				"google"
		);
	}

	private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
		Object responseObj = attributes.get("response");

		if (!(responseObj instanceof Map<?, ?> response)) {
			throw new IllegalArgumentException("Naver response structure invalid: " + attributes);
		}

		Object email = response.get("email");
		if (email == null) {
			throw new IllegalArgumentException("Naver email is missing: " + response);
		}

		return new OAuthAttributes(
				(String) response.get("name"),
				(String) email,
				"naver"
		);
	}

	private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
		log.info("📦 Kakao raw attributes: {}", attributes);

		Object accountObj = attributes.get("kakao_account");
		if (!(accountObj instanceof Map<?, ?> account)) {
			log.error("❌ kakao_account 필드 없음");
			return new OAuthAttributes(null, null, "kakao");
		}

		String email = (String) account.get("email");
		if (email == null) {
			log.warn("⚠️ Kakao 응답에 email 없음: {}", account);
		}

		Map<String, Object> profile = (Map<String, Object>) account.get("profile");
		String nickname = profile != null ? (String) profile.get("nickname") : null;

		return new OAuthAttributes(nickname, email, "kakao");
	}
}
