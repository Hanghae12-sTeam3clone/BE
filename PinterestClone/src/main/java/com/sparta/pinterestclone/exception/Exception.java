package com.sparta.pinterestclone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Exception {

        INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다"),
        NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "토큰을 찾을 수 없습니다"),
        NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당하는 유저가 존재하지 않습니다"),
        DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일 입니다"),
        DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재 합니다"),
        INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
        NOT_FOUND_PIN(HttpStatus.BAD_REQUEST, "해당 하는 PIN을 찾을 수 없습니다"),
        JWT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"다시 로그인 해주세요"),
        NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다"),
        NOT_MATCH_AUTHORIZATION(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다");



        private final HttpStatus httpStatus;
        private final String msg;

}
