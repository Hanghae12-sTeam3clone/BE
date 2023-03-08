package com.sparta.pinterestclone.domain.like.controller;

import com.sparta.pinterestclone.config.ApiDocumentResponse;
import com.sparta.pinterestclone.domain.like.dto.PinLikeResponseDto;
import com.sparta.pinterestclone.domain.like.service.PinLikeService;
import com.sparta.pinterestclone.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pins")
@ApiDocumentResponse
public class PinLikeController {

    private final PinLikeService pinLikeService;

    //좋아요
    @PostMapping("/{pinId}/likes")
    public PinLikeResponseDto likeup(@PathVariable Long pinId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return pinLikeService.likeup(pinId, userDetails);
    }

    //좋아요 취소
    @DeleteMapping("/{pinId}/likes")
    public PinLikeResponseDto likedown(@PathVariable Long pinId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return pinLikeService.likedown(pinId, userDetails);
    }
}
