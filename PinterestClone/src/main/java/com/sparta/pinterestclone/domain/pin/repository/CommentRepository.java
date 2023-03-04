package com.sparta.pinterestclone.domain.pin.repository;

import com.sparta.pinterestclone.domain.pin.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
