package com.sparta.pinterestclone.domain.pin.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;

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
