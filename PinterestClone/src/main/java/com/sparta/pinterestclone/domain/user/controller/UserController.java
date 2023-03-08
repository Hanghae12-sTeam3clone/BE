package com.sparta.pinterestclone.domain.user.controller;

import com.sparta.pinterestclone.auth.refreshtoken.dto.ApiResponseDto;
import com.sparta.pinterestclone.auth.refreshtoken.response.SuccessResponse;
import com.sparta.pinterestclone.domain.user.dto.LoginRequestDto;
import com.sparta.pinterestclone.domain.user.dto.LoginResponseDto;
import com.sparta.pinterestclone.domain.user.dto.SignupRequestDto;
import com.sparta.pinterestclone.domain.user.service.UserService;
import com.sparta.pinterestclone.utils.dto.MessageDto;

import com.sparta.pinterestclone.config.ApiDocumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto,response);
    }

//    회원 토큰 갱신
    @GetMapping("/token")
    public ApiResponseDto<SuccessResponse> issuedToken(HttpServletRequest request, HttpServletResponse response){
        return userService.issueToken(request,response);
    }
}
