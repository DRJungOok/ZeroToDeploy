package com.jungook.zerotodeploy.oauth;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity.Role;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private static final Logger log = LoggerFactory.getLogger(PrincipalOauth2UserService.class);
	private final JoinUserRepo joinUserRepo;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("🧪 userRequest.getClientRegistration().getRegistrationId() = " + userRequest.getClientRegistration().getRegistrationId());

		OAuth2User oAuth2User = super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		log.info("🟡 registrationId = {}", registrationId);
		log.info("✅ [{}] OAuth2 attributes received: {}", userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		oAuth2User.getAttributes().forEach((key, value) -> System.out.println("🔑 " + key + " : " + value));

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
		String email = attributes.getEmail();

		if (email == null) {
			throw new OAuth2AuthenticationException("이메일을 가져올 수 없습니다.");
		}

		JoinUserEntity user = joinUserRepo.findByEmail(email)
				.orElseGet(() -> {
					JoinUserEntity newUser = JoinUserEntity.builder()
							.email(email)
							.userName(email)
							.role(Role.ROLE_USER)
							.build();
					return joinUserRepo.save(newUser);
				});

		Map<String, Object> unifiedAttributes = new HashMap<>(oAuth2User.getAttributes());
		unifiedAttributes.put("email", email);

		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
				unifiedAttributes,
				"email"
		);
	}

}
