package com.jungook.zerotodeploy.repository;

import com.jungook.zerotodeploy.model.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RegisterRepo extends JpaRepository<RegisterEntity, Long> {
    Optional<RegisterEntity> findByUsername(String username);
    Optional<RegisterEntity> findByEmail(String email);
}
