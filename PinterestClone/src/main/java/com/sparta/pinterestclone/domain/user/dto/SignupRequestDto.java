package com.sparta.pinterestclone.domain.user.dto;


import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignupRequestDto {

    @NotNull(message = "아이디는 필수 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}", message = "아이디는 소문자 4-10자 이내 입니다.")
    private String username;

    @NotNull(message = "닉네임은 필수 값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2-10자 이내 입니다.")
    private String nickname;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9\\d`~!@#$%^&*()-_=+]{8,24}$", message = "비밀번호는 대소문자 및 특수문자 포함 8-24자 이내 입니다.")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
