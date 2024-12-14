package com.springboot.board.qna.controller;

import com.springboot.board.qna.dto.ReplyDto;
import com.springboot.board.qna.entity.Reply;
import com.springboot.board.qna.mapper.QnaReplyMapper;
import com.springboot.board.qna.service.QnaReplyService;
import com.springboot.board.qna.service.QnaService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v11/board/qna")
@RestController
public class QnaReplyController {
    private final QnaReplyService qnaReplyService;
    private final QnaService qnaService;
    private final MemberService memberService;
    private final QnaReplyMapper mapper;

    public QnaReplyController(QnaReplyService qnaReplyService, QnaService qnaService, MemberService memberService, QnaReplyMapper mapper) {
        this.qnaReplyService = qnaReplyService;
        this.qnaService = qnaService;
        this.memberService = memberService;
        this.mapper = mapper;
    }

    //리플
    @PostMapping("/reply/{qna-id}")
    public ResponseEntity createQnaReply(@PathVariable("qna-id") long qnaId,
                                         @RequestBody ReplyDto.Post qnaReplyDto,
                                         @AuthenticationPrincipal Object email){
        qnaReplyDto.setQnaId(qnaId);
        if (email != null){
            Member member = memberService.findVerifiedMember(email.toString());
            qnaReplyDto.setMemberId(member.getMemberId());
        }
        else {
            qnaReplyDto.setMemberId(qnaReplyDto.getMemberId());
        }

        ReplyDto.Response response =
                mapper.replyResponseToReply(qnaReplyService.createQnaReply(mapper.replyToReplyPostDto(qnaReplyDto)));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PatchMapping("/reply/{qna-id}")
    public ResponseEntity editQnaReply(@PathVariable("qna-id") long qnaId,
                                       @RequestBody ReplyDto.Patch qnaReplyDto,
                                       @AuthenticationPrincipal Object email){
        qnaReplyDto.setQnaId(qnaId);

        memberService.checkSameMember(email.toString(), qnaReplyDto.getMemberId());

        ReplyDto.Response response =
                mapper.replyResponseToReply(qnaReplyService.aditQnaReply(mapper.replyToReplyPatchDto(qnaReplyDto)));

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/reply/{qna-id}")
    public ResponseEntity deleteReply(@PathVariable("qna-id") long qnaId,
                                      @AuthenticationPrincipal Object email){

        memberService.checkSameMember(email.toString(),  qnaService.findQna(qnaId).getMember().getMemberId());

        qnaReplyService.deleteQnaReply(new Reply(qnaId));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
