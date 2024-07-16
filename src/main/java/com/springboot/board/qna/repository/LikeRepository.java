package com.springboot.board.qna.repository;

import com.springboot.board.qna.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
