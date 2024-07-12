package com.springboot.board.qna.mapper;

import com.springboot.board.qna.dto.QnaDto;
import com.springboot.board.qna.dto.ReplyDto;
import com.springboot.board.qna.entity.Qna;
import com.springboot.board.qna.entity.Reply;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QnaReplyMapper {
    default Reply replyToReplyPostDto(ReplyDto.Post reply){
        Reply newReply = new Reply();
        newReply.setQna(new Qna(reply.getQnaId()));
        newReply.setBody(reply.getBody());
        newReply.setMember(new Member(reply.getMemberId()));

        return newReply;
    };
    default Reply replyToReplyPatchDto(ReplyDto.Patch reply){
        Reply newReply = new Reply();
        newReply.setId(reply.getReplyId());
        newReply.setQna(new Qna(reply.getQnaId()));
        newReply.setBody(reply.getBody());
        newReply.setMember(new Member(reply.getMemberId()));

        return newReply;
    };
    ReplyDto.Response replyResponseToReply(Reply reply);
}
