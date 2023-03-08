package com.sparta.pinterestclone.domain.mysave.dto;

import com.sparta.pinterestclone.domain.comment.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.mysave.entity.MySave;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MySaveResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private int likeCount;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;

    private List<MySave> mySaveList;


    @Builder
    public MySaveResponseDto(Pin pin, List<CommentResponseDto> comments, List<MySave> mySaveList) {
        id = pin.getId();
        title = pin.getTitle();
        content = pin.getContent();
        nickname = pin.getUser().getNickname();
        likeCount = pin.getPinLikes().size();
        image = pin.getImage();
        createdAt = pin.getCreatedAt();
        modifiedAt = pin.getModifiedAt();
        this.comments = comments;

        this.mySaveList = mySaveList;
    }

    public static MySaveResponseDto of(Pin pin, MySave mySave) {
        return MySaveResponseDto.builder()
                .mySaveList(mySave.getPin().getMySaves())
                .pin(pin)
                .build();
    }
}
