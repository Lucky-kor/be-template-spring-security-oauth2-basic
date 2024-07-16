package com.springboot.board.qna.mapper;

import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.entity.Qna;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    default Like liketoids(long qnaId, long memberId){
        Like like = new Like();
        like.setQna(new Qna(qnaId));
        like.addMember(new Member(memberId));
        return like;
    }
}
