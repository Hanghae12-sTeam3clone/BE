package com.sparta.pinterestclone.domain.pin.service;


import com.sparta.pinterestclone.domain.pin.dto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.dto.ApiResponse;
import com.sparta.pinterestclone.domain.comment.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.comment.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.user.repository.UserRepository;
import com.sparta.pinterestclone.domain.user.service.UserService;
import com.sparta.pinterestclone.utils.dto.MessageDto;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import com.sparta.pinterestclone.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static com.sparta.pinterestclone.exception.Exception.*;


@Service
@RequiredArgsConstructor
public class PinService {


    private final PinRepository pinRepository;
    private final S3Uploader s3Uploader;
    private final ApiResponse apiResponse;
    private final UserService userService;
    private final UserRepository userRepository;

    // Pin 저장
    @Transactional
    public MessageDto create(User user, PinRequestDto pinRequestDto) throws IOException {

        String image = s3Uploader.upload(pinRequestDto.getImage());
        //  작성 글 저장
        Pin pin = pinRepository.save(Pin.of(pinRequestDto, image, user));

        return new MessageDto("핀 저장 성공", HttpStatus.OK);
    }

//     Pin 전체 조회하기
    @Transactional
    public List<PinResponseDto> getPintList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pin> pins = pinRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<PinResponseDto> pinResponseDtos = getDtoList(pins);
        return pinResponseDtos;
    }

    // Pin 상세 조회하기
    public PinResponseDto getIdPin(Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new ApiException(NOT_FOUND_PIN)
        );
        pin.getComments().sort(Comparator.comparing(Comment::getCreatedAt).reversed());
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment c : pin.getComments()) {
            commentResponseDtos.add(new CommentResponseDto(c));
        }
        return PinResponseDto.of(pin, commentResponseDtos);
    }

    // Pin 상세 수정
    @Transactional
    public ResponseEntity<PinResponseDto> update(User user, Long pinId, PinRequestDto pinRequestDto) throws IOException {
        Optional<Pin> pin = pinRepository.findById(pinId);

        if(pin.isEmpty()) {
            throw new ApiException(NOT_FOUND_PIN);
        }

        String image = "";
        image = apiResponse.checkNullPinRequestDto(pin.get(), pinRequestDto, image);

        if (checkId(user, pin.get())) {
            pin.get().update(pinRequestDto, image);

            return ResponseEntity.ok().body(PinResponseDto.of(pin.get()));

            } else {
            throw new ApiException(NOT_MATCH_AUTHORIZATION);
        }
    }

    //  Pin 상세 삭제
    public MessageDto delete(User user, Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new ApiException(NOT_FOUND_PIN)
        );
        if (checkId(user, pin)) {
            pinRepository.deleteById(pinId);
            return MessageDto.builder()
                    .msg("성공")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } else {
            throw new ApiException(NOT_MATCH_AUTHORIZATION);
        }
    }

//     Pin 검색 하기
    public List<PinResponseDto> searchPosts(String keyword , int page ,int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pin> pins = pinRepository.findByTitleContaining(pageable,keyword);
        List<PinResponseDto> pinResponseDtos = getDtoList(pins);
        return pinResponseDtos;
    }

    // 마이페이지 내가 만든 게시물 조회
    @Transactional
    public List<PinResponseDto> getMyPage(User user) {

        List<Pin> pins = pinRepository.findByUserId(user.getId());
        List<PinResponseDto> pinList = new ArrayList<>();

        for (Pin p : pins) {
            pinList.add(PinResponseDto.of(p));
        }
        return pinList;

    }

    // 전체 리스트 가져오기 메서드
    private static List<PinResponseDto> getDtoList(Page<Pin> pins) {
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

    // 사용자 확인 메서드
    private static boolean checkId(User user, Pin pin) {
        return Objects.equals(user.getId(), pin.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN;
    }



}

