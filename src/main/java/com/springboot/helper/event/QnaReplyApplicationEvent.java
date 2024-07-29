package com.springboot.helper.event;

import com.springboot.board.qna.entity.Qna;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class QnaReplyApplicationEvent extends ApplicationEvent {
    private Qna qna;
    public QnaReplyApplicationEvent(Object source, Qna qna) {
        super(source);
        this.qna = qna;
    }
}
