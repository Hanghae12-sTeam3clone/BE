package com.sparta.pinterestclone.domain.pin.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.pinterestclone.domain.comment.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.comment.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PinResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private int likeCount;
    private String imageMain;
    private String imageDetail;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;
    private boolean isLike;

    @Builder
    public PinResponseDto(Pin pin, List<CommentResponseDto> comments, boolean isLike) {
        id = pin.getId();
        title = pin.getTitle();
        content = pin.getContent();
        nickname = pin.getUser().getNickname();
        likeCount = pin.getPinLikes().size();
        this.imageMain = pin.getImageMain();
        this.imageDetail = pin.getImageDetail();
        createdAt = pin.getCreatedAt();
        modifiedAt = pin.getModifiedAt();
        this.comments = comments;
        this.isLike =isLike;
    }

    public static PinResponseDto of(Pin pin , List<CommentResponseDto> commentResponseDtos, boolean isLike){
        return PinResponseDto.builder()
                .pin(pin)
                .comments(commentResponseDtos)
                .isLike(isLike)
                .build();
    }

    public static PinResponseDto of(Pin pin, List<CommentResponseDto> commentResponseDtos){

        return PinResponseDto.builder()
                .pin(pin)
                .comments(commentResponseDtos)
                .build();
    }
    public static PinResponseDto of(Pin pin){
        return PinResponseDto.builder()
                .pin(pin)
                .build();
    }

    @QueryProjection
    public PinResponseDto(Pin pin ,List<CommentResponseDto> commentResponseDtos) {
        id = pin.getId();
        title = pin.getTitle();
        content = pin.getContent();
        nickname = pin.getUser().getNickname();
        likeCount = pin.getPinLikes().size();
        imageMain = pin.getImageMain();
        imageDetail = pin.getImageDetail();
        createdAt = pin.getCreatedAt();
        modifiedAt = pin.getModifiedAt();
        comments = commentResponseDtos;
    }
}
