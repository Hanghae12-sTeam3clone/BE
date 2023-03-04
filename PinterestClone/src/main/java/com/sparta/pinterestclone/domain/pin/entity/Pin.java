package com.sparta.pinterestclone.domain.pin.entity;

import com.sparta.pinterestclone.domain.pin.RequestDto.PinRequestDto;
import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Pin extends Timestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "pin")
    private List<Comment> comments;



    @Builder
    public Pin(PinRequestDto requestDto,User user) {        //  사용자 클래스 변경 확인 요망
        this.title = title;
        this.content = content;
        this.image = image;
        this.user = user;
    }

    public static Pin of(PinRequestDto requestDto,String image ,User user) {//  사용자 클래스 변경 확인 요망
        return Pin.builder()
                .requestDto(requestDto)
                .user(user)
                .build();
    }


}
