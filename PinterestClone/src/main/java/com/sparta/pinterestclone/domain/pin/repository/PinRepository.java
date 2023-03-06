package com.sparta.pinterestclone.domain.pin.repository;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {

    Page<Pin> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Pin> findAll(Pageable pageable);

    Page<Pin> findByTitleContaining(Pageable pageable , String keyword);

    List<Pin> findByUserId(Long id);
}
