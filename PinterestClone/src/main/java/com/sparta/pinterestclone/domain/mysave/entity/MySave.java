package com.sparta.pinterestclone.domain.mysave.entity;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MySave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PIN_ID", nullable = false)
    private Pin pin;

    @Builder
    public MySave(User user, Pin pin) {
        this.user = user;
        this.pin = pin;
    }

    public static MySave of(User user, Pin pin) {
        return MySave.builder()
                .user(user)
                .pin(pin)
                .build();
    }
}