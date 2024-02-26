package com.project.SmartSplit.repository;

import com.project.SmartSplit.entity.RefreshToken;
import com.project.SmartSplit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User byEmail);

    void deleteByUser(User byEmail);

    Optional<RefreshToken> findByToken(String token);
}
