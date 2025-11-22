package com.jungook.zerotodeploy.joinMember;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JoinUserService {
	private final JoinUserRepo joinUserRepo;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public void registerUser(JoinUserDTO joinUserDTO) {
		if (joinUserRepo.existsByEmail(joinUserDTO.getEmail())) {
			throw new RuntimeException("email already exists");
		}
		if (joinUserRepo.existsByUserName(joinUserDTO.getUserName())) {
			throw new RuntimeException("user name already exists");
		}
		if (joinUserRepo.existsByNickname(joinUserDTO.getNickname())) {
			throw new RuntimeException("nickname already exists");
		}

		JoinUserEntity joinUserEntity = JoinUserEntity.builder()
				.userName(joinUserDTO.getUserName())
				.nickname(joinUserDTO.getNickname())
				.email(joinUserDTO.getEmail())
				.password(bCryptPasswordEncoder.encode(joinUserDTO.getPassword()))
				.role(JoinUserEntity.Role.valueOf("ROLE_USER"))
				.build();
 
		joinUserRepo.save(joinUserEntity);
	}
}
