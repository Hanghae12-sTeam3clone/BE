package com.sparta.pinterestclone.domain.pin.ResponseDto;

import com.sparta.pinterestclone.domain.pin.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto (Comment comment){
        id = comment.getId();
        nickname = comment.getUser().getNickname();
        this.comment = comment.getComment();
        createdAt = comment.getCreatedAt();
        modifiedAt = comment.getModifiedAt();
    }
}
