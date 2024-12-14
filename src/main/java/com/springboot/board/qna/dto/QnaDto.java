package com.springboot.board.qna.dto;

import com.springboot.board.qna.entity.Qna;
import lombok.*;

import java.time.LocalDateTime;

public class QnaDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post{
        private long memberId;
        private String title;
        private String body;
        private int lock;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch{
        private long memberId;
        private long qnaId;
        private String title;
        private String body;
        private int lock;

        public void setMemberId(long memberId){
            this.memberId = memberId;
        }
        public void setQnaId(long qnaId){
            this.qnaId = qnaId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private Long qnaId;
        private String title;
        private String body;
        private String reply;
        private String memberName;
        private LocalDateTime createdAt;
        private LocalDateTime replyCreatedAt;
        private Qna.QnaStatus qnaStatus;
        private int lock;
        private int like;
        private int view;
        // 내가 좋아요를 누른 게시글인지
        private int isLike;
    }
}
