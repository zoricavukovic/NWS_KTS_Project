package com.example.serbUber.service;

import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Environment env;

    public EmailService(
            final JavaMailSender javaMailSender,
            final Environment env
    ) {
        this.javaMailSender = javaMailSender;
        this.env = env;
    }

    @Async
    public void sendMail(String email, String mailSubj, String mailText) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        //mail.setTo("zoka200015@gmail.com");

        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject(mailSubj);
        mail.setText(mailText);
        javaMailSender.send(mail);
    }
}
