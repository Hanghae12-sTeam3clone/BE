package com.sparta.pinterestclone.domain.mysave.repository;

import com.sparta.pinterestclone.domain.mysave.entity.MySave;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MySaveRepository extends JpaRepository <MySave, Long> {
    Optional<MySave> findByPinAndUser(Pin pin, User user);
    List<MySave> findByUserId(Long id);

}
