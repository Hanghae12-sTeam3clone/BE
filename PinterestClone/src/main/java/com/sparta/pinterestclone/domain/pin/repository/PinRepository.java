package com.sparta.pinterestclone.domain.pin.repository;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {


    List<Pin> findAllByOrderByCreatedAtDesc();

    List<Pin> findByTitleContaining(String keyword);

}
