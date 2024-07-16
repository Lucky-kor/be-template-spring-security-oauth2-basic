package com.springboot.board.qna.controller;

import com.springboot.board.qna.dto.QnaDto;
import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.mapper.LikeMapper;
import com.springboot.board.qna.mapper.QnaMapper;
import com.springboot.board.qna.service.QnaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RequestMapping("/v11/board/qna")
@RestController
public class LikeController {

    private final LikeMapper mapper;
    private final QnaService qnaService;

    private final QnaMapper qnaMapper;

    public LikeController(LikeMapper mapper, QnaService qnaService, QnaMapper qnaMapper) {
        this.mapper = mapper;
        this.qnaService = qnaService;
        this.qnaMapper = qnaMapper;
    }

    //좋아요
    @PostMapping("/like/{qna-id}")
    public ResponseEntity likeQna(@Positive @PathVariable("qna-id") long qnaId,
                                  @Positive @RequestParam long memberId){
        Like like = mapper.liketoids(qnaId, memberId);
        return new ResponseEntity(qnaMapper.qnaResponseToqna(qnaService.likedQna(like)), HttpStatus.OK);
    }
}
