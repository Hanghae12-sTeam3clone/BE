package com.sparta.pinterestclone.domain.mysave.controller;

import com.sparta.pinterestclone.auth.refreshtoken.response.SuccessResponse;
import com.sparta.pinterestclone.auth.security.UserDetailsImpl;
import com.sparta.pinterestclone.domain.mysave.service.MySaveService;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MySaveController {

    private final MySaveService mySaveService;


    //회원 게시물 저장 및 취소
    @PostMapping("mypage/save/{pinId}")
    public SuccessResponse getSave(@PathVariable Long pinId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mySaveService.getSave(pinId, userDetails.getUser());
    }

    //리스트
    @GetMapping("/mypage/save")
    public List<PinResponseDto> getView(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mySaveService.getView(userDetails.getUser());
    }
}

