package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.exceptions.SocialException;
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

    public void sendOtp(String email, Otp otp) throws SocialException {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("OTP");
            simpleMailMessage.setText("OTP Code: " + otp.getValue());

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new SocialException(String.format("Send OTP failed for email: %s due to %s", email, e.getMessage()), e);
        }
    }
}
