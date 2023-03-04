package com.sparta.pinterestclone.domain.pin.ResponseDto;

import com.sparta.pinterestclone.domain.pin.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PinResponseDto {

    private String title;
    private String content;
    private String nickname;
    private int likeCount;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;

    @Builder
    public PinResponseDto(Pin pin, List<CommentResponseDto> comments) {
        title = pin.getTitle();
        content = pin.getContent();
        nickname = pin.getUser().getNickname();
//        likeCount = pin.getPinlikes().size();
        image = pin.getImage();
        createdAt = pin.getCreatedAt();
        modifiedAt = pin.getModifiedAt();
        this.comments = comments;
    }

    public static PinResponseDto of(Pin pin , List<CommentResponseDto> commentResponseDtos){
        return PinResponseDto.builder()
                .pin(pin)
                .comments(commentResponseDtos)
                .build();
    }
}
