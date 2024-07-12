package com.springboot.board.qna.controller;

import com.springboot.board.qna.dto.ReplyDto;
import com.springboot.board.qna.mapper.QnaReplyMapper;
import com.springboot.board.qna.service.QnaReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v11/board/qna")
@RestController
public class QnaReplyController {
    private final QnaReplyService qnaReplyService;
    private final QnaReplyMapper mapper;

    public QnaReplyController(QnaReplyService qnaReplyService, QnaReplyMapper mapper) {
        this.qnaReplyService = qnaReplyService;
        this.mapper = mapper;
    }

    //리플
    @PostMapping("/reply/{qna-id}")
    public ResponseEntity createQnaReply(@PathVariable("qna-id") long qnaId,
                                         @RequestBody ReplyDto.Post qnaReplyDto){
        qnaReplyDto.setQnaId(qnaId);
        qnaReplyDto.setMemberId(qnaReplyDto.getMemberId());
        ReplyDto.Response response =
                mapper.replyResponseToReply(qnaReplyService.createQnaReply(mapper.replyToReplyPostDto(qnaReplyDto)));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PatchMapping("/reply/{qna-id}")
    public ResponseEntity editQnaReply(@PathVariable("qna-id") long qnaId,
                                       @RequestBody ReplyDto.Patch qnaReplyDto){
        qnaReplyDto.setQnaId(qnaId);
        ReplyDto.Response response =
                mapper.replyResponseToReply(qnaReplyService.aditQnaReply(mapper.replyToReplyPatchDto(qnaReplyDto)));

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/reply/{qna-id}")
    public ResponseEntity deleteReply(@PathVariable("qna-id") long qnaId){
        qnaReplyService.deleteQnaReply(qnaId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
