package com.sparta.pinterestclone.domain.user.dto;

import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private String msg;
    private int statusCode;
    private String nickname;

    public LoginResponseDto(String msg, int statusCode, String nickname) {
        this.msg = msg;
        this.statusCode = statusCode;
        this.nickname = nickname;
    }
}
