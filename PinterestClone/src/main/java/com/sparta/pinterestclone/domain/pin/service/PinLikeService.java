package com.sparta.pinterestclone.domain.pin.service;

import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.PinLikeResponseDto;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.entity.PinLike;
import com.sparta.pinterestclone.domain.pin.repository.PinLikeRepository;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PinLikeService {


    private final PinLikeRepository pinLikeRepository;
    private final PinRepository pinRepository;

    @Transactional
    public PinLikeResponseDto likeup(Long id, UserDetailsImpl userDetails) {

        //id값으로 게시물 확인
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시물이 없습니다.")
        );

        // 매개변수 pin과 getUser로 PinLike에 있는지 확인 후 없다면 repository에 저장
        Optional<PinLike> found = pinLikeRepository.findByPinAndUser(pin, userDetails.getUser());
        if (found.isEmpty()) {
            PinLike pinLike = PinLike.of(userDetails.getUser(),pin);
            pinLikeRepository.save(pinLike);
        }
        return PinLikeResponseDto.of(true,"좋아요 성공", HttpStatus.OK);
    }


    @Transactional
    public PinLikeResponseDto likedown(Long id, UserDetailsImpl userDetails) {

        //id값으로 게시물 확인
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시물이 없습니다.")
        );

        // 매개변수 pin과 getUser로 PinLike에 있는지 확인 후 없다면 repository에서 삭제
        Optional<PinLike> found = pinLikeRepository.findByPinAndUser(pin, userDetails.getUser());
        if (!found.isEmpty()) {
            pinLikeRepository.delete(found.get());
        }
        return PinLikeResponseDto.of(false,"좋아요 취소", HttpStatus.OK);
    }

}