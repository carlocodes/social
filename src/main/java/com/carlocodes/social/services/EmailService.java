package com.carlocodes.social.services;

import com.carlocodes.social.exceptions.SocialException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String from;

    public void sendOtp(String email, String password) throws SocialException {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("OTP");
            simpleMailMessage.setText("OTP Code: " + password);

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new SocialException(String.format("Send OTP failed for email: %s due to %s", email, e.getMessage()), e);
        }
    }
}
