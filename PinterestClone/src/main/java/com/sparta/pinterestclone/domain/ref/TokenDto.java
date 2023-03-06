package com.sparta.pinterestclone.domain.ref;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDto {
    private String Authorization;
    private String Refresh_Token;

    public TokenDto(String authorization, String refresh_Token) {
        Authorization = authorization;
        Refresh_Token = refresh_Token;
    }
}
