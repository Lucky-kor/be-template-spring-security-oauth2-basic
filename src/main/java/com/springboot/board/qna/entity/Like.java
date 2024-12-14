package com.springboot.board.qna.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "LIKES")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="QNA_ID")
    private Qna qna;

    public void addMember(Member member){
        this.setMember(member);

        if(!member.getLikeQnaList().contains(member)){
            member.addLikeQna(this);
        }
    }

    public void addQna(Qna qna){
        this.setQna(qna);

        if(!qna.getLikes().contains(this)){
            qna.addLike(this);
        }
    }
}
