package com.sparta.pinterestclone.domain.pin.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.pinterestclone.domain.comment.dto.CommentResponseDto;
import com.sparta.pinterestclone.domain.comment.entity.Comment;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.pinterestclone.domain.pin.entity.QPin.pin;

@RequiredArgsConstructor
@Repository
public class PinRepositoryCustomImpl implements CustomPinRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PinResponseDto> findAllByOrderByCreatedAtDesc(Pageable pageable){

        QueryResults<Pin> results = queryFactory
                .select(pin)
                .from(pin)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .orderBy(pin.createdAt.desc())
                .fetchResults();

        // DTO로 반환
        List<PinResponseDto> contents = new ArrayList<>();
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for(Pin pin : results.getResults()) {
            List<Comment> comments = pin.getComments();
            for(Comment comment : comments){
                commentResponseDtos.add(CommentResponseDto.from(comment));
            }
            contents.add(new PinResponseDto(pin, commentResponseDtos));
        }

        // 다음 페이지 여부 판단하는 로직
        boolean hasNext = false;
        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(contents, pageable, hasNext);
    }


}
