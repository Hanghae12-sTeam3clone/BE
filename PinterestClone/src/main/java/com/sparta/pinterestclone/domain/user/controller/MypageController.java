package com.sparta.pinterestclone.domain.user.controller;

import com.sparta.pinterestclone.config.ApiDocumentResponse;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.service.PinService;
import com.sparta.pinterestclone.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ApiDocumentResponse
public class MypageController {

    private final PinService pinService;

    @GetMapping("/mypage")
    public ResponseEntity<List<PinResponseDto>> myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok()
                .body(pinService.getMyPage(userDetails.getUser()));
    }
}
