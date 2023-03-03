package com.sparta.pinterestclone.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageDto {

    private String msg;
    private int statusCode;

    public MessageDto(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.statusCode = httpStatus.value();
    }
}
