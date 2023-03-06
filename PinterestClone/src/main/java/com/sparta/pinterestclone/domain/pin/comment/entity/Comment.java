package com.sparta.pinterestclone.domain.pin.comment.entity;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.entity.Timestamp;
import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PIN_ID", nullable = false)
    private Pin pin;


    //연관관계 편의 메소드
    //사용이유 혹여의 이유로 setPin(pin1),(pin2)두변 설정될 경우 버그가 발생 이를 방지하기위해
    public void setPin(Pin pin) {
        if (this.pin != null) { // 기존에 이미 Pin이 존제한다면
            this.pin.getComments().remove(this);//관계를 끊는다.
        }
        this.pin = pin;
        pin.getComments().add(this); //연결성 가진다.
    }
    @Builder
    public Comment(String comment, User user, Pin pin) {
        this.comment = comment;
        this.user = user;
        this.pin = pin;
    }

    public void update(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public static Comment of(String comment, Pin pin , User user){
      return builder()
              .comment(comment)
              .pin(pin)
              .user(user).build();
    }

}
