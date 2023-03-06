package com.sparta.pinterestclone.domain.pin.entity;

import com.sparta.pinterestclone.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PinLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "PIN_ID", nullable = false)
    private Pin pin;

    @Builder
    public PinLike(User user, Pin pin) {
        this.user = user;
        this.pin = pin;
    }

    public static PinLike of(User user, Pin pin) {
        return PinLike.builder()
                .user(user)
                .pin(pin)
                .build();
    }
}
