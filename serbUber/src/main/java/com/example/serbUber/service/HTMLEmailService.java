package com.example.serbUber.service;

import com.example.serbUber.exception.MailCannotBeSentException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class HTMLEmailService {
    private JavaMailSender mailSender;


    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String from, String to, String subject, String msg) throws MailCannotBeSentException {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new MailCannotBeSentException(from);
        }
    }
}
