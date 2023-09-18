package com.carlocodes.social.services;

import com.carlocodes.social.enums.OtpEnum;
import com.carlocodes.social.exceptions.SocialException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {
    public String generateOtp() throws SocialException {
        try {
            Random random = new Random();
            StringBuilder value = new StringBuilder();

            for (int i = 0; i < OtpEnum.OTP_LENGTH.getValue(); i++) {
                value.append(random.nextInt(10));
            }

            return value.toString();
        } catch (Exception e) {
            throw new SocialException(String.format("Generate OTP failed due to %s", e.getMessage()), e);
        }
    }
}
