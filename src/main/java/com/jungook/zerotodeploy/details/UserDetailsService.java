//package com.jungook.zerotodeploy.details;
//
//import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
//import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailsService  {
//
//	private final JoinUserRepo joinUserRepo;
//
//	@Override
//	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//		JoinUserEntity user = joinUserRepo.findByUserId(userId)
//				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자 없음: " + userId));
//
//		return User.builder()
//				.username(user.getUserId())         // 로그인 ID
//				.password(user.getPassword())       // 암호화된 비밀번호
//				.roles("USER")                      // 역할 없으면 기본값 설정
//				.build();
//	}
//}
//
