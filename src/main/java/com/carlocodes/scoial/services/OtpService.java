package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.exceptions.SocialException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    // TODO: Maybe move these values into an enum
    // So we can use in other classes
    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRATION_MINUTES = 10;

    public Otp generateOtp() throws SocialException {
        try {
            Random random = new Random();
            StringBuilder value = new StringBuilder();

            for (int i = 0; i < OTP_LENGTH; i++) {
                value.append(random.nextInt(10));
            }

            LocalDateTime expiration = LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);

            return new Otp(value.toString(), expiration);
        } catch (Exception e) {
            throw new SocialException(String.format("Generate OTP failed due to %s", e.getMessage()), e);
        }
    }
}
