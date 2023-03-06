package com.sparta.pinterestclone.domain.user.controller;

import com.sparta.pinterestclone.domain.ref.ApiResponseDto;
import com.sparta.pinterestclone.domain.ref.SuccessResponse;
import com.sparta.pinterestclone.domain.user.dto.LoginRequestDto;
import com.sparta.pinterestclone.domain.user.dto.SignupRequestDto;
import com.sparta.pinterestclone.domain.user.service.UserService;
import com.sparta.pinterestclone.dto.MessageDto;

import com.sparta.pinterestclone.utils.ApiDocumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@ApiDocumentResponse
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MessageDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 로그인
    @PostMapping("/login")
    public ApiResponseDto<SuccessResponse> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto,response);
    }
}
