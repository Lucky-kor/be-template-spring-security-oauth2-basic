package com.springboot.board.qna.repository;

import com.springboot.board.qna.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    Page<Qna> findAllByQnaStatusNot(Pageable pageable, Qna.QnaStatus qnaStatus);
}
