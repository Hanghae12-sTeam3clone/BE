package com.sparta.pinterestclone.domain.pin.service;


import com.sparta.pinterestclone.domain.pin.ResponseDto.ApiResponse;
import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.pin.RequestDto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.exception.ErrorCode;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PinService {


    private final PinRepository pinRepository;
    private final S3Uploader s3Uploader;
    private final ApiResponse apiResponse;


    @Transactional
    public MessageResponseDto create(User user, PinRequestDto pinRequestDto) throws IOException { //  , UserDetailsImpl userDetails 추가
        String image = "";
//        System.out.println(pinRequestDto.getImage());
//        if (pinRequestDto.getImage() == null) {
//            image = "";
//        } else {
            image = s3Uploader.upload(pinRequestDto.getImage());
//        }
        //                      작성 글 저장
        Pin pin = pinRepository.save(Pin.of(pinRequestDto, image, user));        //      , user 추가

//                      PinResponseDto 로 변환 후 responseEntity body 에 담아 반환
        return apiResponse.success(ErrorCode.CONSTRUCT_SUCCESS.getMessage());
        //  CONSTRUCT_SUCCESS.getMsg() 예외 메세지
    }


    //          전체 조회하기
    @Transactional
    public List<PinResponseDto> getPins() {
        List<Pin> pins = pinRepository.findAllByOrderByCreatedAtDesc();
        List<PinResponseDto> pinResponseDtos = new ArrayList<>();
        for (Pin p : pins) {
            p.getComments().sort(Comparator.comparing(Comment::getCreatedAt).reversed());
            List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
            for (Comment c : p.getComments()) {
                commentResponseDtos.add(new CommentResponseDto(c));
            }
            pinResponseDtos.add(PinResponseDto.of(p, commentResponseDtos));
        }
        return pinResponseDtos;
    }
}

