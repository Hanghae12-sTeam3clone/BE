package com.sparta.pinterestclone.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {

    private final String msg;
    private final int statusCode;

    public static ResponseEntity<ErrorResponse> toResponseEntity(Exception exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.builder()
                        .msg(exception.getMsg())
                        .statusCode(exception.getHttpStatus().value())
                        .build());

    }

}
