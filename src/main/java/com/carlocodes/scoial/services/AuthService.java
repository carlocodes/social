package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.AuthDto;
import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.utilities.InMemoryOtpCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
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

            if (!userService.existsByEmail(email)) {
                User user = new User();
                user.setEmail(email);
                userService.save(user);
            }

            if (doesEmailHaveAnExistingOtp(email)) {
                InMemoryOtpCache.remove(email);
            }

            Otp otp = otpService.generateOtp();
            InMemoryOtpCache.put(email, otp);
            emailService.sendOtp(email, otp);
        } catch (SocialException e) {
            throw new SocialException(String.format("Register user with email: %s failed due to %s", authDto.getEmail(), e.getMessage()), e);
        }
    }

    public void verify(AuthDto authDto) throws SocialException {
        try {
            String email = authDto.getEmail();
            String password = authDto.getPassword();

            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isEmpty()) {
                throw new SocialException(String.format("User with email: %s does not exist!", email));
            }

            User user = optionalUser.get();
            Otp storedOtp = InMemoryOtpCache.get(email);

            if (Objects.isNull(storedOtp) || !password.equals(storedOtp.getValue())) {
                throw new SocialException("OTP does not match or does not exist!");
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime storedOtpExpiration = storedOtp.getExpiration();

            if (now.isAfter(storedOtpExpiration)) {
                throw new SocialException("OTP expired!");
            }

            if (!user.isVerified()) {
                user.setVerified(true);
                userService.save(user);
            }

            // TODO: Authenticate and Authorize
            // Return JWT

            InMemoryOtpCache.remove(email);
        } catch (SocialException e) {
            throw new SocialException(String.format("Verify user with email: %s failed due to %s", authDto.getEmail(), e.getMessage()), e);
        }
    }

    private boolean doesEmailHaveAnExistingOtp(String email) {
        return Objects.nonNull(InMemoryOtpCache.get(email));
    }
}
