package com.sparta.pinterestclone.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private final ErrorCode errorcode;

    public ApiException(ErrorCode Errorcode){
        this.errorcode = getErrorcode();
    }
}