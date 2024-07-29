package com.springboot.board.qna.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Indexed
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false, length = 50)
    @FullTextField
    private String title;

    @Column(nullable = false)
    private String body;

    @Column
    private String imageURL;

    @Column
    private int likeCount = 0;

    @Enumerated(value = EnumType.STRING)
    private QnaStatus qnaStatus = QnaStatus.QUESTION_REGISTERED;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.PERSIST)
    private Reply reply;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 비밀글여부
    @Column
    private int lock = 0;

    @Column
    private int view = 0;

    @OneToMany(mappedBy = "qna", cascade = CascadeType.MERGE)
    private List<Like> likes;

    public Qna(long qnaId){
        this.qnaId = qnaId;
    }

    public void addMember(Member member){
        this.member = member;
        if(!member.getQnas().contains(this)){
            member.setQna(this);
        }
    }

    public void addReply(Reply reply){
        this.reply = reply;
        if(reply.getQna() != this){
            reply.addQna(this);
        }
    }

    public void addLike(Like like){
        if (!this.likes.contains(like))
            this.likes.add(like);

        if (like.getQna() != this)
            like.addQna(this);
    }

    public void removeLiek(Like like){
        Optional<Like> deleteLike =  likes.stream()
                .filter(x -> x.getMember().getMemberId() == like.getMember().getMemberId()
                        && x.getQna().getQnaId() == like.getQna().getQnaId()).findAny();
        likes.remove(deleteLike.get());
    }

    public enum QnaStatus{
        QUESTION_REGISTERED("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETED("질문 삭제 상태"),
        QUESTION_DEACTIVED("질문 비활성화 상태: 회원 탈퇴 시, 질문 비활성화 상태");

        @Getter
        private String description;
        QnaStatus(String description){
            this.description = description;
        }
    }

    //정렬 기준
    public static Sort SortType(String sortType){
        switch (sortType){
            case "SORT_OLD":
                return Sort.by("qnaId");
            case "SORT_VIEW_MAX":
                return Sort.by("view").descending();
            case "SORT_VIEW_MIN":
                return Sort.by("view");
            case "SORT_LIKE_MANY":
                return Sort.by("likes").descending();
            case  "SORT_LIKE_MIN":
                return Sort.by("likes");
            default:
                //기본 최신글 정렬
                return Sort.by("qnaId").descending();
        }
    };
}
