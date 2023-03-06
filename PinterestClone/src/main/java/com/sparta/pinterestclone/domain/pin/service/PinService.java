package com.sparta.pinterestclone.domain.pin.service;


import com.sparta.pinterestclone.domain.pin.ResponseDto.ApiResponse;
import com.sparta.pinterestclone.domain.pin.ResponseDto.BooleanIdCheck;
import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.CommentResponseDto;
import com.sparta.pinterestclone.domain.pin.RequestDto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.exception.ErrorCode;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private final BooleanIdCheck booleanIdCheck;

    @Transactional
    public MessageResponseDto create(User user, PinRequestDto pinRequestDto) throws IOException { //  , UserDetailsImpl userDetails 추가
        String image = "";
        System.out.println(pinRequestDto.getImage());
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
        List<PinResponseDto> pinResponseDtos = getDtoList(pins);
        return pinResponseDtos;
    }



    //          상세 수정
    @Transactional
    public MessageResponseDto update(User user, Long id, PinRequestDto pinRequestDto)throws IOException {
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 없습니다.")
        );
        String image = "";
        image = apiResponse.checkNullPinRequestDto(pin,pinRequestDto,image);
        if(booleanIdCheck.CheckId(user,pin)){
            pin.update(pinRequestDto, image);
            return MessageResponseDto.builder()
                    .msg("성공")
                    .code(HttpStatus.OK)
                    .build();
        }else{
            throw new IllegalArgumentException("오류입니다.");
        }
    }


//              상세 게시글 삭제
    public MessageResponseDto delete(User user, Long id) {
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
        if(booleanIdCheck.CheckId(user,pin)){
            pinRepository.deleteById(id);
            return MessageResponseDto.builder()
                    .msg("성공")
                    .code(HttpStatus.OK)
                    .build();
        }else{
            throw new IllegalArgumentException("삭제를 실패했습니다.");
        }
    }


    public List<PinResponseDto> searchPosts(String keyword) {
        List<Pin> pins = pinRepository.findByTitleContaining(keyword);
        List<PinResponseDto> pinResponseDtos = getDtoList(pins);
        return pinResponseDtos;
    }

    public PinResponseDto getIdPin(Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
        pin.getComments().sort(Comparator.comparing(Comment::getCreatedAt).reversed());
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment c : pin.getComments()) {
            commentResponseDtos.add(new CommentResponseDto(c));
        }
        return PinResponseDto.of(pin,commentResponseDtos);
    }

    
    @Transactional
    public List<PinResponseDto> getMyPage(User user) {

        List<Pin> pins = pinRepository.findByUserId(user.getId());
        List<PinResponseDto> pinList = new ArrayList<>();

        for (Pin p : pins) {
            pinList.add(PinResponseDto.of(p));
        }
        return pinList;

    }

    private static List<PinResponseDto> getDtoList(List<Pin> pins) {
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

