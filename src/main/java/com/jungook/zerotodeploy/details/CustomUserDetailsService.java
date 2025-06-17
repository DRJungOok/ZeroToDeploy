package com.jungook.zerotodeploy.details;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

       private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

       private final JoinUserRepo joinUserRepo;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
               log.info("🔍 loadUserByUsername 호출됨 - userId: {}", name);

		JoinUserEntity user = joinUserRepo.findByUserName(name)
				.orElseThrow(() -> {
                                       log.warn("❌ 사용자 ID 없음: {}", name);
					return new UsernameNotFoundException("해당 사용자 없음: " + name);
				});

               log.info("✅ 사용자 정보 조회됨: {}", user.getUserName());
               log.info("✅ DB에서 불러온 권한: {}", user.getRole());

		return new CustomUserDetails(user);
	}
}