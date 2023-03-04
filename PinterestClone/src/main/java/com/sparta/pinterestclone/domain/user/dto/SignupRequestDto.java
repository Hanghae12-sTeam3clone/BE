package com.sparta.pinterestclone.domain.user.dto;


import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignupRequestDto {

    @NotNull(message = "이메일을 입력 하세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식에 맞게 입력하세요")
    private String username;

    @NotNull(message = "닉네임을 입력 하세요")
    @Size(min = 2, max = 10, message = "닉네임은 2-10자 이내 입니다.")
    private String nickname;

    @NotNull(message = "비밀번호를 입력 하세요")
    @Pattern(regexp = "^[a-zA-Z0-9\\d`~!@#$%^&*()-_=+]{8,24}$", message = "비밀번호는 대소문자 및 특수문자 포함 8-24자 이내 입니다.")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
