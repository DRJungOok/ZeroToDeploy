package com.jungook.zerotodeploy.joinMember;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinUserService {
	private final JoinUserRepo joinUserRepo;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public void registerUser(JoinUserDTO joinUserDTO) {
		if (joinUserRepo.existsByEmail(joinUserDTO.getEmail())) {
			throw new RuntimeException("user id already exists");
		}
		if (joinUserRepo.existsByUserId(joinUserDTO.getUserId())) {
			throw new RuntimeException("user id already exists");
		}

		JoinUserEntity joinUserEntity = JoinUserEntity.builder()
				.userId(joinUserDTO.getUserId())
				.email(joinUserDTO.getEmail())
				.password(bCryptPasswordEncoder.encode(joinUserDTO.getPassword()))
				.build();

		joinUserRepo.save(joinUserEntity);
	}
}
