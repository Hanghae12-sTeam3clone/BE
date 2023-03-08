package com.sparta.pinterestclone.domain.pin.service;


import com.sparta.pinterestclone.domain.like.entity.PinLike;
import com.sparta.pinterestclone.domain.like.repository.PinLikeRepository;
import com.sparta.pinterestclone.domain.pin.dto.PinRequestDto;
import com.sparta.pinterestclone.domain.comment.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.comment.entity.Comment;
import com.sparta.pinterestclone.domain.like.entity.PinLike;
import com.sparta.pinterestclone.domain.like.repository.PinLikeRepository;
import com.sparta.pinterestclone.domain.pin.dto.ApiResponse;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import com.sparta.pinterestclone.exception.ApiException;
import com.sparta.pinterestclone.utils.dto.MessageDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.*;

import static com.sparta.pinterestclone.exception.Exception.NOT_FOUND_PIN;
import static com.sparta.pinterestclone.exception.Exception.NOT_MATCH_AUTHORIZATION;


@Service
@RequiredArgsConstructor
public class PinService {


    private final PinRepository pinRepository;
    private final S3Uploader s3Uploader;
    private final ApiResponse apiResponse;


    private final PinLikeRepository pinLikeRepository;

    // Pin 저장
    @Transactional
    public MessageDto create(User user, PinRequestDto pinRequestDto) throws Exception {
        // 이미지를 나눠서 업로드 해야한다.
        String imageMain = s3Uploader.mainUpload(resizeImage(pinRequestDto.getImage())); // 리사이즈한 이미지를 S3에 업로드 하기
        String imageDetail = s3Uploader.detailUpload(pinRequestDto.getImage()); // 일반 이미지 파일
        //  작성 글 저장
        Pin pin = pinRepository.save(Pin.of(pinRequestDto, imageMain, imageDetail, user));

        return new MessageDto("핀 저장 성공", HttpStatus.OK);
        
    }

//     Pin 전체 조회하기
    @Transactional
    public Slice<PinResponseDto> getPintList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<PinResponseDto> pins = pinRepository.findAllByOrderByCreatedAtDesc(pageable);
        return pins;
    }

    // Pin 상세 조회하기
    public PinResponseDto getIdPin(Long pinId, User user) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new ApiException(NOT_FOUND_PIN)
        );
        pin.getComments().sort(Comparator.comparing(Comment::getCreatedAt).reversed());
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment c : pin.getComments()) {
            commentResponseDtos.add(new CommentResponseDto(c));
        }
        PinLike pinLike = pinLikeRepository.findByPinIdAndUser(pinId, user);
        if(pinLike==null){
            return PinResponseDto.of(pin, commentResponseDtos, false);
        }else {
            return PinResponseDto.of(pin, commentResponseDtos, true);
        }
    }

    // Pin 상세 수정
    @Transactional
    public ResponseEntity<MessageDto> update(User user, Long pinId, PinRequestDto pinRequestDto) throws Exception {
        Optional<Pin> pin = pinRepository.findById(pinId);

        if(pin.isEmpty()) {
            throw new ApiException(NOT_FOUND_PIN);
        }

        String image = "";
        image = apiResponse.checkNullPinRequestDto(pin.get(), pinRequestDto, image);

        if (checkId(user, pin.get())) {
            pin.get().update(pinRequestDto, image);

            return ResponseEntity.ok().body(new MessageDto("핀 수정 성공", HttpStatus.OK));

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

//     전체 리스트 가져오기 메서드
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

    // multipart로 받은 이미지 파일 리사이징 해서 파일 만들기
    public File resizeImage(MultipartFile file) throws IOException {
        // 리사이징은 MltipartFile로 변환할 수 없다.
        //1. MltipartFile 에서 BufferedImage로 변환
        // BufferedImage에 있는 메서드로 우리가 받은 MltipartFile을 BufferedImage로 변환 후 이미지의 가로세로 추출.
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        int originWidth = originalImage.getWidth();
        int originHeight = originalImage.getHeight();

        // 받은 file 비율 최적화 시키기 ( 파일 사이즈를 줄여서 데이터 크기 감량 )
        int newWidth = 220;
        int newHeight = 0;
        if (originWidth > newWidth) {
            newHeight = (originHeight * newWidth) / originWidth; // 이미지 비율로 변환
        }

        //2. Graphics2D 사용으로 리사이징
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();


//       이미지 파일을 가져와 크기를 조정한 후, 새 파일에 조정된 이미지를 작성하는 메소드입니다. 다음과 같은 동작을 수행.
//
//        1.원본 파일과 같은 이름으로 새 File 객체를 생성합니다.
//        2.createNewFile() 메소드를 사용하여 해당 이름으로 새 파일을 생성합니다. 파일이 이미 존재하면 새 파일을 만들지 않습니다.
//        3.ImageIO.write() 메소드를 사용하여 조정된 이미지를 ByteArrayOutputStream 객체에 쓰기합니다.
//        4.ByteArrayOutputStream로부터 InputStream을 생성합니다.
//        5.Files.copy() 메소드를 사용하여 InputStream을 2 단계에서 만든 새 파일에 복사하고, 이미 파일이 존재하는 경우 덮어씁니다.
//          새로운 File 객체를 반환합니다.
//        6.코드 실행 중에 예외가 발생하면 RuntimeException이 throw됩니다.
//          이미지의 크기를 조정하고 조정된 이미지를 새 파일에 저장하는데 사용한다.
        File outputfile = new File(file.getOriginalFilename());
        try {
            if (outputfile.createNewFile()) {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                String type = file.getContentType().substring(file.getContentType().indexOf("/")+1);
                ImageIO.write(resizedImage, type, bos);

                InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());

                Files.copy(inputStream, outputfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return outputfile;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;

    }



}

