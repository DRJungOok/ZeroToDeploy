package com.jungook.zerotodeploy.joinMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JoinUserRepo extends JpaRepository<JoinUserEntity, Long> {
	boolean existsByUserName(String userId);
	boolean existsByEmail(String email);
	Optional<JoinUserEntity> findByUserName(String name);
	Optional<JoinUserEntity> findByEmail(String email);
	Optional<JoinUserEntity> findByUserName(String name, String password);
}
