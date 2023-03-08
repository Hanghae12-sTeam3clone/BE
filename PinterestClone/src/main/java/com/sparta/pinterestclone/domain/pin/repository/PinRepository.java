package com.sparta.pinterestclone.domain.pin.repository;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PinRepository extends JpaRepository<Pin, Long> ,CustomPinRepository{

//    Page<Pin> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Pin> findAll(Pageable pageable);

    Optional<Pin> findById(Long pinId);

    Page<Pin> findByTitleContaining(Pageable pageable , String keyword);

    List<Pin> findByUserId(Long id);
}
