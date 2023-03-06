package com.sparta.pinterestclone.auth.refreshtoken.repository;

import com.sparta.pinterestclone.auth.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findAllByEmail(String email);
}
