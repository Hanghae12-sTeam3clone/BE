package com.sparta.pinterestclone.domain.pin.entity;

import com.sparta.pinterestclone.domain.mysave.entity.MySave;
import com.sparta.pinterestclone.domain.pin.dto.PinRequestDto;
import com.sparta.pinterestclone.domain.comment.entity.Comment;
import com.sparta.pinterestclone.domain.like.entity.PinLike;
import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Pin extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imageMain;

    @Column(nullable = false)
    private String imageDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "pin")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "pin")
    private List<PinLike> pinLikes = new ArrayList<>();

    @OneToMany(mappedBy = "pin")
    private List<MySave> mySaves = new ArrayList<>();

    @Builder
    public Pin(PinRequestDto requestDto, User user, String imageMain, String imageDetail) {        //  사용자 클래스 변경 확인 요망
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imageMain = imageMain;
        this.imageDetail = imageDetail;
        this.user = user;
    }

    public static Pin of(PinRequestDto requestDto, String imageMain, String imageDetail,User user) {//  사용자 클래스 변경 확인 요망
        return Pin.builder()
                .requestDto(requestDto)
                .imageMain(imageMain)
                .imageDetail(imageDetail)
                .user(user)
                .build();
    }

    public void update(PinRequestDto pinRequestDto, String image) {
        this.title = pinRequestDto.getTitle();
        this.content = pinRequestDto.getContent();
        this.imageDetail = image;
    }
}
