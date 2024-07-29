package com.springboot.board.qna.mapper;

import com.springboot.board.qna.dto.QnaDto;
import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.entity.Qna;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QnaMapper {
    default Qna qnaToQnaPostDto(QnaDto.Post qnaDto){
        Qna newQna = new Qna();
        newQna.setMember(new Member(qnaDto.getMemberId()));
        newQna.setTitle(qnaDto.getTitle());
        newQna.setBody(qnaDto.getBody());
        newQna.setLock(qnaDto.getLock());

        return newQna;
    };
    default Qna qnaToQnaPatchDto(QnaDto.Patch qnaDto){
        Qna newQna = new Qna();
        newQna.setMember(new Member(qnaDto.getMemberId()));
        newQna.setQnaId(qnaDto.getQnaId());
        newQna.setTitle(qnaDto.getTitle());
        newQna.setBody(qnaDto.getBody());
        newQna.setLock(qnaDto.getLock());

        return newQna;
    };

    default QnaDto.Response qnaResponseToqna(Qna qna){

        QnaDto.Response response;

        int isLike = 0;

        if(qna.getLikes() != null){
            isLike = qna.getLikes().stream()
                    .filter(x-> x.getMember().getMemberId() == qna.getMember().getMemberId())
                    .count() == 0 ? 0 : 1;
        };


        if(qna.getReply() != null){
            response = QnaDto.Response.builder()
                    .qnaId(qna.getQnaId())
                    .body(qna.getBody())
                    .title(qna.getTitle())
                    .lock(qna.getLock())
                    .createdAt(qna.getCreatedAt())
                    .memberName(qna.getMember().getName())
                    .reply(qna.getReply().getBody())
                    .replyCreatedAt(qna.getReply().getCreatedAt())
                    .like(qna.getLikeCount())
                    .qnaStatus(qna.getQnaStatus())
                    .view(qna.getView())
                    .isLike(isLike)
                    .build();
        }
        else {
            response = QnaDto.Response.builder()
                    .qnaId(qna.getQnaId())
                    .body(qna.getBody())
                    .title(qna.getTitle())
                    .lock(qna.getLock())
                    .createdAt(qna.getCreatedAt())
                    .memberName(qna.getMember().getName())
                    .like(qna.getLikeCount())
                    .view(qna.getView())
                    .qnaStatus(qna.getQnaStatus())
                    .isLike(isLike)
                    .build();
        }

        return response;
    }
}
