package com.jungook.zerotodeploy.Oauth;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuthService extends DefaultOAuth2UserService {

	private final JoinUserRepo joinUserRepo;

	@Override
	@SuppressWarnings("unchecked")
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		String email = null;
		String nickname = null;

		switch (registrationId) {
			case "kakao" -> {
				Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
				Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
				email = (String) kakaoAccount.get("email");
				nickname = (String) profile.get("nickname");

				System.out.println(">>> [DEBUG] registrationId = " + registrationId);
				System.out.println(">>> [DEBUG] attributes = " + attributes);
			}
			case "google" -> {
				email = (String) attributes.get("email");
				nickname = (String) attributes.get("name");
			}
			case "naver" -> {
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				email = (String) response.get("email");
				nickname = (String) response.get("name");
			}
		}

		if (email == null || email.isEmpty()) {
			throw new OAuth2AuthenticationException("소셜 로그인 제공자가 이메일을 제공하지 않았습니다.");
		}

		if (nickname == null || nickname.isEmpty()) {
			nickname = email.split("@")[0];
		}

		Optional<JoinUserEntity> optionalUser = joinUserRepo.findByEmail(email);
		JoinUserEntity user;

		if (optionalUser.isPresent()) {
			user = optionalUser.get();
		} else {
			int duplicateCount = 0;
			String baseNickname = nickname;
			while (joinUserRepo.existsByUserName(nickname)) {
				duplicateCount++;
				nickname = baseNickname + "_" + duplicateCount;
			}

			user = JoinUserEntity.builder()
					.email(email)
					.userName(nickname)
					.password(null)
					.role(JoinUserEntity.Role.ROLE_USER)
					.build();
			user = joinUserRepo.save(user);
		}

		String userNameAttributeName = userRequest.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
				attributes,
				userNameAttributeName
		);
	}
}
