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
			throw new RuntimeException("user id already exists");
		}
		if (joinUserRepo.existsByUserName(joinUserDTO.getUserId())) {
			throw new RuntimeException("user id already exists");
		}

		JoinUserEntity joinUserEntity = JoinUserEntity.builder()
				.userName(joinUserDTO.getUserId())
				.email(joinUserDTO.getEmail())
				.password(bCryptPasswordEncoder.encode(joinUserDTO.getPassword()))
				.role(JoinUserEntity.Role.valueOf("ROLE_USER"))
				.build();
 
		joinUserRepo.save(joinUserEntity);
	}
}
