package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.service.PinLikeService;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PinLikeController {

    private final PinLikeService pinLikeService;

    //좋아요
    @PostMapping("/likes/{id}")
    public MessageResponseDto likeup(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return pinLikeService.likeup(id, userDetails);
    }

    //좋아요 취소
    @DeleteMapping("/likes/{id}")
    public MessageResponseDto likedown(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return pinLikeService.likedown(id, userDetails);
    }
}
