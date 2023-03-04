package com.sparta.pinterestclone.domain.pin.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageResponseDto {
    private String msg;
    private HttpStatus code;

    @Builder
    public MessageResponseDto(String msg,HttpStatus code){
        this.msg = msg;
        this.code = code;
    }
}
