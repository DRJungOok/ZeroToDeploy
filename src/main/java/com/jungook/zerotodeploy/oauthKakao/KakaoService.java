package com.jungook.zerotodeploy.oauthKakao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService extends DefaultOAuth2UserService {

}
