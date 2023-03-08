package com.sparta.pinterestclone.domain.pin.repository;

import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomPinRepository {
    Slice<PinResponseDto> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
