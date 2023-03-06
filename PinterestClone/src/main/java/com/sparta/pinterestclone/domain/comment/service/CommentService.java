package com.sparta.pinterestclone.domain.comment.service;

import com.sparta.pinterestclone.domain.comment.dto.CommentRequestDto;
import com.sparta.pinterestclone.domain.comment.entity.Comment;
import com.sparta.pinterestclone.domain.comment.repository.CommentRepository;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import com.sparta.pinterestclone.utils.dto.MessageDto;
import com.sparta.pinterestclone.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.pinterestclone.exception.Exception.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PinRepository pinRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public MessageDto createComment(Long pinId, CommentRequestDto requestDto, User user) {

        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new ApiException(NOT_FOUND_PIN)
        );
        Comment comment = commentRepository.save(Comment.of(requestDto.getComment(), pin, user));
        comment.setPin(pin);
        return new MessageDto("댓글작성 완료", HttpStatus.OK);
    }

    public MessageDto updateComment(Long commentId, CommentRequestDto requestDto, User user){
        Comment comment =  commentRepository.findById(commentId).orElseThrow(
                ()  -> new ApiException(NOT_FOUND_COMMENT)
        );
        Optional<Comment> found = commentRepository.findByIdAndUser(commentId,user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER){
            throw new ApiException(NOT_MATCH_AUTHORIZATION);
        }
        comment.update(requestDto.getComment(),user);
        return new MessageDto("댓글 수정 성공", HttpStatus.OK);
    }

    public MessageDto deleteComment(Long commentId , User user){
        Comment comment =  commentRepository.findById(commentId).orElseThrow(
                ()  -> new ApiException(NOT_FOUND_COMMENT)
        );
        Optional<Comment> found = commentRepository.findByIdAndUser(commentId,user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER){
            throw new ApiException(NOT_MATCH_AUTHORIZATION);
        }
        commentRepository.deleteById(commentId);
        return new MessageDto("댓글 삭제 성공", HttpStatus.OK);
    }

}
