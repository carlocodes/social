package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.dtos.UserDto;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.enums.OtpEnum;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.repositories.UserRepository;
import com.carlocodes.scoial.utilities.InMemoryOtpCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       OtpService otpService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    public void register(UserDto userDto) throws SocialException {
        try {
            String email = userDto.getEmail();

            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setEmail(email);
                userRepository.save(user);
            }

            if (doesEmailHaveAnExistingOtp(email)) {
                InMemoryOtpCache.remove(email);
            }

            Otp otp = otpService.generateOtp();
            InMemoryOtpCache.put(email, otp);
            emailService.sendOtp(email, otp);
        } catch (SocialException e) {
            throw new SocialException(String.format("Register failed for email: %s due to %s", userDto.getEmail(), e.getMessage()), e);
        }
    }

    public void verify(UserDto userDto) throws SocialException {
        try {
            String email = userDto.getEmail();
            String otp = userDto.getOtp();

            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                throw new SocialException(String.format("User not found for email: %s", email));
            }

            User user = optionalUser.get();
            Otp storedOtp = InMemoryOtpCache.get(email);

            if (Objects.isNull(storedOtp) || !otp.equals(storedOtp.getValue())) {
                throw new SocialException("OTP does not match or does not exist!");
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime storedOtpExpiration = storedOtp.getExpiration();

            if (now.isAfter(storedOtpExpiration)) {
                throw new SocialException("OTP expired!");
            }

            if (!user.getVerified()) {
                user.setVerified(true);
                userRepository.save(user);
            }

            // TODO: Authenticate and Authorize
            // Return JWT

            InMemoryOtpCache.remove(email);
        } catch (SocialException e) {
            throw new SocialException(String.format("Verify failed for email: %s due to %s", userDto.getEmail(), e.getMessage()), e);
        }
    }

    private boolean doesEmailHaveAnExistingOtp(String email) {
        return Objects.nonNull(InMemoryOtpCache.get(email));
    }
}
