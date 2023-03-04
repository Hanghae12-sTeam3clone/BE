package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.RequestDto.CommentRequestDto;
import com.sparta.pinterestclone.domain.pin.service.CommentService;
import com.sparta.pinterestclone.dto.MessageDto;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/pins/{pinId}/comments")
    public MessageDto createComment(@PathVariable Long pinId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(pinId, requestDto, userDetails.getUser());
    }


    @PutMapping("/pins/comments/{commentId}")
    public MessageDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(commentId,requestDto,userDetails.getUser());
    }

    @DeleteMapping("/pins/comments/{commentId}")
    public MessageDto deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(commentId,userDetails.getUser());
    }
}
