package com.jungook.zerotodeploy.Oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class CustomOAuthService extends DefaultOAuth2UserService {
	@Override
	@SuppressWarnings("unchecked")
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		Map<String, Object> attributes = oAuth2User.getAttributes();

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

		String email = (String) kakaoAccount.get("email");
		String nickname = (String) profile.get("nickname");
		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
				oAuth2User.getAttributes(),
				"id"
		);
	}
}
