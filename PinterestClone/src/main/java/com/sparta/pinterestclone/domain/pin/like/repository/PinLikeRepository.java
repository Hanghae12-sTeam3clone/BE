package com.sparta.pinterestclone.domain.pin.like.repository;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.like.entity.PinLike;
import com.sparta.pinterestclone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinLikeRepository extends JpaRepository<PinLike, Long> {
    Optional<PinLike> findByPinAndUser(Pin pin, User user);
}
