package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.dto.CommentRequestDto;
import com.sparta.pinterestclone.domain.pin.service.CommentService;
import com.sparta.pinterestclone.dto.MessageDto;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/pins/{pinId}/comments")
    public MessageDto createComment(@PathVariable Long pinId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(pinId, requestDto, userDetails.getUser());
    }
}
