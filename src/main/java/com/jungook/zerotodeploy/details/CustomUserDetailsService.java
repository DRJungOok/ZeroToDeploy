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
		JoinUserEntity user = joinUserRepo.findByUserId(userId)
				.orElseThrow(() -> {
					System.out.println("로그인 실패 - 존재하지 않는 사용자 ID: " + userId);
					return new UsernameNotFoundException("해당 사용자 없음: " + userId);
				});
		System.out.println("🔐 유저 정보 확인됨: " + user.getUserId() + ", 권한: " + user.getRole());
		return User.builder()
				.username(user.getUserId())
				.password(user.getPassword())
				.authorities(user.getRole())
				.build();
	}
}