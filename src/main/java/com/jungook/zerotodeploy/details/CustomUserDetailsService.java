// CustomUserDetailsService.java (수정된 UserDetailsService)
package com.jungook.zerotodeploy.details;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final JoinUserRepo joinUserRepo;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		System.out.println("🔍 loadUserByUsername 호출됨 - userId: " + userId);

		JoinUserEntity user = joinUserRepo.findByUserId(userId)
				.orElseThrow(() -> {
					System.out.println("❌ 사용자 ID 없음: " + userId);
					return new UsernameNotFoundException("해당 사용자 없음: " + userId);
				});

		System.out.println("✅ 사용자 정보 조회됨: " + user.getUserId());
		System.out.println("✅ DB에서 불러온 권한: " + user.getRole());
		return User.builder()
				.username(user.getUserId())
				.password(user.getPassword())
				.authorities(String.valueOf(user.getRole()))
				.build();
	}
}