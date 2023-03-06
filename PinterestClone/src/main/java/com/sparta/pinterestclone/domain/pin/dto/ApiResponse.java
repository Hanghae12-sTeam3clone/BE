package com.sparta.pinterestclone.domain.pin.dto;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.service.S3Uploader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Component
@RequiredArgsConstructor
public class ApiResponse {
    private final S3Uploader s3Uploader;

    public MessageResponseDto success(String msg){
        return MessageResponseDto.builder()
                .msg(msg)
                .code(HttpStatus.OK)
                .build();
    }

    public String checkNullPinRequestDto(Pin pin, PinRequestDto pinRequestDto, String image)throws IOException{
        if(pinRequestDto.getImage()==null||pinRequestDto.getImage().isEmpty()){
            image = pin.getImage();
        }else{
            image = s3Uploader.upload(pinRequestDto.getImage());
        }if(pinRequestDto.getTitle()==null){
            pinRequestDto.setTitle(pin.getTitle());
        }if(pinRequestDto.getContent()==null){
            pinRequestDto.setContent(pin.getContent());
        }
        return image;
    }

}
