package com.sparta.pinterestclone.domain.pin.dto;

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
