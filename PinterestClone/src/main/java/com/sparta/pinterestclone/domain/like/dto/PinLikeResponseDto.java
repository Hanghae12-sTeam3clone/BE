package com.sparta.pinterestclone.domain.like.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PinLikeResponseDto {

    private boolean isLike;
    private String msg;
    private HttpStatus status;

    @Builder
    public PinLikeResponseDto(boolean isLike, String msg, HttpStatus status) {
        this.isLike = isLike;
        this.msg = msg;
        this.status = status;
    }

    public static PinLikeResponseDto of(boolean isLike, String msg, HttpStatus status){
        return PinLikeResponseDto.builder()
                .isLike(isLike)
                .msg(msg)
                .status(status)
                .build();
    }
}
