package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.AuthDto;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.enums.OtpEnum;
import com.carlocodes.scoial.exceptions.SocialException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthService(UserService userService,
                       OtpService otpService,
                       EmailService emailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    public void register(AuthDto authDto) throws SocialException {
        try {
            String email = authDto.getEmail();
            String password = otpService.generateOtp();
            LocalDateTime now = LocalDateTime.now();

            Optional<User> optionalUser = userService.findByEmail(email);

            User user = optionalUser.orElse(new User());

            user.setEmail(email);
            user.setPassword(password);
            user.setDateTime(now);

            userService.save(user);

            emailService.sendOtp(email, password);
        } catch (SocialException e) {
            throw new SocialException(String.format("Register user with email: %s failed due to %s", authDto.getEmail(), e.getMessage()), e);
        }
    }

    public void verify(AuthDto authDto) throws SocialException {
        try {
            String email = authDto.getEmail();
            String password = authDto.getPassword();

            Optional<User> optionalUser = userService.findByEmail(email);
            User user = optionalUser.orElseThrow(() -> new SocialException(String.format("User with email: %s does not exist!", email)));
            String storedPassword = user.getPassword();

            if (!password.equals(storedPassword)) {
                throw new SocialException("OTP does not match!!");
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationTime = user.getDateTime().plusMinutes(OtpEnum.OTP_EXPIRATION_MINUTES.getValue());

            if (now.isAfter(expirationTime)) {
                throw new SocialException("OTP expired!");
            }

            if (!user.isVerified()) {
                user.setVerified(true);
                userService.save(user);
            }

            // TODO: Authenticate and Authorize
            // Return JWT
        } catch (SocialException e) {
            throw new SocialException(String.format("Verify user with email: %s failed due to %s", authDto.getEmail(), e.getMessage()), e);
        }
    }
}
