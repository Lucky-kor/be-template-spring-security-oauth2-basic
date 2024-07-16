package com.springboot.board.qna.service;

import com.springboot.board.qna.entity.Qna;
import com.springboot.board.qna.entity.Reply;
import com.springboot.board.qna.repository.QnaReplyRepository;
import com.springboot.board.qna.repository.QnaRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QnaReplyService {

    private final QnaReplyRepository repository;
    private final MemberService memberService;
    private final QnaService qnaService;

    public QnaReplyService(QnaReplyRepository repository, MemberService memberService, QnaService qnaService) {
        this.repository = repository;
        this.memberService = memberService;
        this.qnaService = qnaService;
    }

    //등록
    public Reply createQnaReply(Reply reply){
        Qna qna = qnaService.verifyQna(reply.getQna().getQnaId());

        reply.setQna(qna);

        //답변있으면 에러
        if(qnaService.chakedCompleteQna(qna.getQnaId()))
            throw new BusinessLogicException(ExceptionCode.ANSWERED_QNA);

        //운영진이 아닐 경우


        reply.setMember(memberService.findMember(reply.getMember().getMemberId()));

        Reply saveReply = repository.save(reply);

        qnaService.createQnaReply(reply);

        return saveReply;
    }

    //삭제
    public void deleteQnaReply(Reply reply){
        Reply deleteReply = verifyQnaReply(reply.getId());
        qnaService.deleteQnaReply(deleteReply.getQna().getQnaId());
        repository.delete(deleteReply);
    }

    //수정
    public Reply aditQnaReply(Reply reply){
        Reply findreply = verifyQnaReply(qnaService.verifyQna(reply.getQna().getQnaId()).getReply().getId());
        findreply.setBody(reply.getBody());
        Reply saveReply =  repository.save(findreply);
        return saveReply;
    }

    public Reply verifyQnaReply(long replyId){
        Optional<Reply> reply = repository.findById(replyId);
        return reply.orElseThrow(() -> new BusinessLogicException(ExceptionCode.REPLY_NOT_FOUND));
    }
}
