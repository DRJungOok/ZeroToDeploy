package com.jungook.zerotodeploy.joinMember;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinUserRepo extends JpaRepository<JoinUserEntity, Long> {
	boolean existsByUserId(String userId);
	boolean existsByEmail(String email);
	Optional<JoinUserEntity> findByUserId(String id);

	JoinUserEntity findByEmail(String email);
}
