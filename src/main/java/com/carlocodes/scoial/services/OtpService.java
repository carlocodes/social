package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.enums.OtpEnum;
import com.carlocodes.scoial.exceptions.SocialException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    public Otp generateOtp() throws SocialException {
        try {
            Random random = new Random();
            StringBuilder value = new StringBuilder();

            for (int i = 0; i < OtpEnum.OTP_LENGTH.getValue(); i++) {
                value.append(random.nextInt(10));
            }

            LocalDateTime expiration = LocalDateTime.now().plusMinutes(OtpEnum.OTP_EXPIRATION_MINUTES.getValue());

            return new Otp(value.toString(), expiration);
        } catch (Exception e) {
            throw new SocialException(String.format("Generate OTP failed due to %s", e.getMessage()), e);
        }
    }
}
