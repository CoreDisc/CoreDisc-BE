package com.coredisc.application.service.auth;

import com.coredisc.common.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MailService {

    private final RedisUtil redisUtil;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private Long codeExpiration;

    // 인증 코드 생성
    private String createCode() {

        int leftLimit = 48; // 0
        int rightLimit = 122; // z
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 이메일 폼 생성
    private MimeMessage createEmailForm(String email, String code) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.addRecipients(MimeMessage.RecipientType.TO, email);

        message.setSubject("CoreDisc 이메일 인증 코드 안내");

        String body = "";
        body += "<p>회원가입을 위한 이메일 인증을 진행합니다.</p>";
        body += "<p>아래 발급된 인증번호를 입력하여 인증을 완료해주세요.</p>";
        body += "<br>";
        body += "<p>인증번호 " + code + "</p>";
        message.setText(body, "UTF-8", "html");

        // Redis에 해당 인증코드 인증 시간 설정(30분)
        String redisKey = "auth: " + email;
        redisUtil.set(redisKey, code);
        redisUtil.expire(redisKey, codeExpiration, TimeUnit.MILLISECONDS);

        return message;
    }

    // 메일 발송
    @Async
    public void sendEmail(String sendEmail) throws MessagingException {

        String redisKey = "auth: " + sendEmail;
        if (redisUtil.exists(redisKey)) {
            redisUtil.delete(redisKey);
        }

        String authCode = createCode();
        try {
            // 메일 생성
            MimeMessage message = createEmailForm(sendEmail, authCode);
            // 메일 발송
            javaMailSender.send(message);
        } catch (MailSendException e) {
            throw new MailSendException(e.getMessage());
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
    }
}
