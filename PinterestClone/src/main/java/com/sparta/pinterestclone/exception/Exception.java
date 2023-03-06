package com.sparta.pinterestclone.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Exception {

        INVALID_TOKEN(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다."),
        NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST,"토큰을 찾을 수 없습니다."),
        NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"사용자가 존재하지 않습니다."),
        NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST,"게시글을 찾을 수 없습니다."),
        NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST,"댓글을 찾을 수 없습니다."),
        AUTHORIZATION(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 없습니다."),
        DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST,"중복된 사용자 이름 입니다."),
        INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다.")
        ;

        private HttpStatus ecode ;
        private String msg;

        Exception(HttpStatus ecode , String msg) {
            this.ecode = ecode;
            this.msg=msg;
        }

}
