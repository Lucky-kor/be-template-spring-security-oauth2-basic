package com.springboot.board.qna.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @OneToOne
    @JoinColumn(name = "QNA_ID")
    private Qna qna;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void addQna(Qna qna){
        this.qna = qna;
        if(qna.getReply() != this){
            qna.addReply(this);
        }
    }

    public void addMember(Member member){
        this.member = member;
    }

    public Reply(String body) {
        this.body = body;
    }

    public Reply(long id) {
        this.id = id;
    }
}
