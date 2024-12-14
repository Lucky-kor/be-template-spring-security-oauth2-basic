package com.springboot.board.qna.repository;

import com.springboot.board.qna.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaReplyRepository extends JpaRepository<Reply, Long> {
}
