package com.jungook.zerotodeploy.Oauth;

import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
	private final String name;
	private final String email;
	private final String provider;

	public OAuthAttributes(String name, String email, String provider) {
		this.name = name;
		this.email = email;
		this.provider = provider;
	}

	public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
		return switch (registrationId) {
			case "naver" -> ofNaver(attributes);
			case "kakao" -> ofKakao(attributes);
			default -> ofGoogle(attributes);
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
		Object accountObj = attributes.get("kakao_account");

		if (!(accountObj instanceof Map<?, ?> account)) {
			throw new IllegalArgumentException("Kakao account structure invalid: " + attributes);
		}

		Object email = account.get("email");
		if (email == null) {
			throw new IllegalArgumentException("Kakao email is missing: " + account);
		}

		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		return new OAuthAttributes(
				(String) profile.get("nickname"),
				(String) email,
				"kakao"
		);
	}
}
