package com.sparta.pinterestclone.domain.pin.service;

import com.sparta.pinterestclone.domain.pin.dto.CommentRequestDto;
import com.sparta.pinterestclone.domain.pin.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.repository.CommentRepository;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PinRepository pinRepository;
    private final CommentRepository commentRepository;

    public MessageDto createComment(Long pinId, CommentRequestDto requestDto, User user) {

        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("Pin을 찾을 수 없습니다.")
        );
        Comment comment = commentRepository.save(Comment.of(requestDto.getComment(), pin, user));
        comment.setPin(pin);
        return new MessageDto("댓글작성 완료", HttpStatus.OK);
    }
}
