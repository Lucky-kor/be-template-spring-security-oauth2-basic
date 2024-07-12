package com.springboot.member.entity;

import com.springboot.audit.Auditable;
import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.entity.Qna;
import com.springboot.order.entity.Order;
import com.springboot.stamp.Stamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Qna> qnas = new ArrayList<>();

    // 수정된 부분
    @OneToOne(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Stamp stamp;

    // 내가 좋아요를 누른 글 리스트
    @OneToMany(mappedBy = "member")
    private List<Like> likeQnaList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public Member(String email) {
        this.email = email;
    }

    public Member(long memberId){
        this.memberId = memberId;
    }

    public Member(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public void setOrder(Order order) {
        orders.add(order);
        if (order.getMember() != this) {
            order.setMember(this);
        }
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
        if (stamp.getMember() != this) {
            stamp.setMember(this);
        }
    }

    public void setQna(Qna qna){
        if (!qnas.contains(qna))
            qnas.add(qna);
        if(qna.getMember() != this){
            qna.addMember(this);
        }
    }

    public void addLikeQna(Like qna){
        if(!likeQnaList.contains(qna))
            likeQnaList.add(qna);
        if(qna.getMember() != this){
            qna.addMember(this);
        }
    }

    public boolean likedQna(Like like){
        // 아직 like가 저장되지 않았으니 내부의 id값들로 비교
        return likeQnaList.stream()
                .anyMatch(x -> x.getMember().getMemberId() == like.getMember().getMemberId()
                        && x.getQna().getQnaId() == like.getQna().getQnaId());
    }

    public long removeLikeQna(Like like){
        Optional<Like> deleteLike =  likeQnaList.stream()
                .filter(x -> x.getMember().getMemberId() == like.getMember().getMemberId()
                && x.getQna().getQnaId() == like.getQna().getQnaId()).findAny();
        likeQnaList.remove(deleteLike.get());
        return deleteLike.get().getId();
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
           this.status = status;
        }
    }
}
