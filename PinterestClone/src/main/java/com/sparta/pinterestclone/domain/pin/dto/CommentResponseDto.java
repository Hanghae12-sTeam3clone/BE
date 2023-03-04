package com.sparta.pinterestclone.domain.pin.dto;

import com.sparta.pinterestclone.domain.pin.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto (Comment comment ){
        nickname = comment.getUser().getNickname();
        this.comment = comment.getComment();
        createdAt = comment.getCreatedAt();
        modifiedAt = comment.getModifiedAt();
    }
}
