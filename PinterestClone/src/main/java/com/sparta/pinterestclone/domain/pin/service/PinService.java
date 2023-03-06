package com.sparta.pinterestclone.domain.pin.service;


import com.sparta.pinterestclone.domain.pin.dto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.dto.ApiResponse;
import com.sparta.pinterestclone.domain.pin.comment.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.comment.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.dto.MessageDto;
import com.sparta.pinterestclone.exception.ErrorCode;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PinService {


    private final PinRepository pinRepository;
    private final S3Uploader s3Uploader;
    private final ApiResponse apiResponse;

    // Pin 저장
    @Transactional
    public MessageDto create(User user, PinRequestDto pinRequestDto) throws IOException {

        String image = s3Uploader.upload(pinRequestDto.getImage());
        //  작성 글 저장
        Pin pin = pinRepository.save(Pin.of(pinRequestDto, image, user));

        return new MessageDto("핀 저장 성공", HttpStatus.OK);
    }

    // Pin 전체 조회하기
    @Transactional
    public List<PinResponseDto> getPins() {
        List<Pin> pins = pinRepository.findAllByOrderByCreatedAtDesc();
        List<PinResponseDto> pinResponseDtos = getDtoList(pins);
        return pinResponseDtos;
    }

    // Pin 상세 조회하기
    public PinResponseDto getIdPin(Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
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
    public ResponseEntity<PinResponseDto> update(User user, Long id, PinRequestDto pinRequestDto) throws IOException {
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 없습니다.")
        );
        String image = "";
        image = apiResponse.checkNullPinRequestDto(pin, pinRequestDto, image);

        if (checkId(user, pin)) {
            pin.update(pinRequestDto, image);

            return ResponseEntity.ok().body(PinResponseDto.of(pin));

            } else {
            throw new IllegalArgumentException("오류입니다.");
        }
    }

    //  Pin 상세 삭제
    public MessageDto delete(User user, Long id) {
        Pin pin = pinRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
        if (checkId(user, pin)) {
            pinRepository.deleteById(id);
            return MessageDto.builder()
                    .msg("성공")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } else {
            throw new IllegalArgumentException("삭제를 실패했습니다.");
        }
    }

    // Pin 검색 하기
    public List<PinResponseDto> searchPosts(String keyword) {
        List<Pin> pins = pinRepository.findByTitleContaining(keyword);
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

    // 사용자 확인 메서드
    private static boolean checkId(User user, Pin pin) {
        return Objects.equals(user.getId(), pin.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN;
    }


}

