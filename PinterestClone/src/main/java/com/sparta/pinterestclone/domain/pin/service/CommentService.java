package com.sparta.pinterestclone.domain.pin.service;

import com.sparta.pinterestclone.domain.pin.RequestDto.CommentRequestDto;
import com.sparta.pinterestclone.domain.pin.entity.Comment;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.repository.CommentRepository;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import com.sparta.pinterestclone.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PinRepository pinRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public MessageDto createComment(Long pinId, CommentRequestDto requestDto, User user) {

        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("Pin을 찾을 수 없습니다.")
        );
        Comment comment = commentRepository.save(Comment.of(requestDto.getComment(), pin, user));
        comment.setPin(pin);
        return new MessageDto("댓글작성 완료", HttpStatus.OK);
    }

    public MessageDto updateComment(Long commentId, CommentRequestDto requestDto, User user){
        Comment comment =  commentRepository.findById(commentId).orElseThrow(
                ()  -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
        Optional<Comment> found = commentRepository.findByIdAndUser(commentId,user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER){
            throw new IllegalArgumentException("권환이 없습니다.");
        }
        comment.update(requestDto.getComment(),user);
        return new MessageDto("댓글 수정 성공", HttpStatus.OK);
    }

    public MessageDto deleteComment(Long commentId , User user){
        Comment comment =  commentRepository.findById(commentId).orElseThrow(
                ()  -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
        Optional<Comment> found = commentRepository.findByIdAndUser(commentId,user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER){
            throw new IllegalArgumentException("권환이 없습니다.");
        }
        commentRepository.deleteById(commentId);
        return new MessageDto("댓글 삭제 성공", HttpStatus.OK);
    }
}
