package com.sparta.pinterestclone.domain.pin.ResponseDto;

import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Getter
@Component
public class BooleanIdCheck {
    public boolean CheckId(User user, Pin pin){
        return Objects.equals(user.getId(), pin.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN;
    }
}
