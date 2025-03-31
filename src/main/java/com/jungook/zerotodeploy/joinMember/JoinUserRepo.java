package com.jungook.zerotodeploy.joinMember;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinUserRepo extends JpaRepository<JoinUserEntity, Long> {
	boolean existsByUserId(String userId);
	boolean existsByEmail(String email);

	JoinUserEntity findByEmail(String email);
	JoinUserEntity findByUserId(String userId);
}
