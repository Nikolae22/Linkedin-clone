package com.backend.features.authentication.utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;


    public void sendEmail(String email,String subject,String content) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom("noReplay@.com","We");
        helper.setTo(email);

        helper.setSubject(subject);
        helper.setText(content,true);

        javaMailSender.send(message);
    }
}
