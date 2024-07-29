package com.springboot.helper.event;

import com.springboot.board.qna.entity.Qna;
import com.springboot.board.qna.service.QnaService;
import com.springboot.helper.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Configuration
@Component
@Slf4j
public class QnaReplyEventListener {
    private final EmailSender emailSender;

    public QnaReplyEventListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    @EventListener
    public void listen(QnaReplyApplicationEvent event) throws Exception {
        try {
            // 전송할 메시지를 생성했다고 가정.
            String message = "any email message";
            Qna qna = event.getQna();
            qna.getMember().getEmail();
            emailSender.sendEmail(message);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("MailSendException: " + e.getMessage());
        }
    }
}
