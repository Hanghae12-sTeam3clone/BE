package com.sparta.pinterestclone.domain.comment.dto;

import com.sparta.pinterestclone.domain.comment.entity.Comment;
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

    public static CommentResponseDto from(Comment comment){
        return CommentResponseDto.builder()
                .comment(comment)
                .build();

    }
}
