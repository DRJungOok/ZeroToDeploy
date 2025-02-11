package com.jungook.zerotodeploy.user.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RegisterRepo extends JpaRepository<RegisterEntity, Long> {
    Optional<RegisterEntity> findByUsername(String username);
    Optional<RegisterEntity> findByEmail(String email);
}