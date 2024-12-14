package com.springboot.board.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReplyDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post{
        private long qnaId;
        private String body;
        private long memberId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch{
        private long replyId;
        private long qnaId;
        private String body;
        private long memberId;

        public void setQnaId(long qnaId) {
            this.qnaId = qnaId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private String body;
    }
}
