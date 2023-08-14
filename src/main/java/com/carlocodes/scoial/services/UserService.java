package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.dtos.UserDto;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.repositories.UserRepository;
import com.carlocodes.scoial.utilities.InMemoryOtpCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                User user = new User();

                user.setEmail(email);
                user.setVerified(false);

                userRepository.save(user);

                Otp otp = otpService.generateOtp();
                // TODO: Add logic to check if there is an existing otp in place already
                // If so, remove the existing OTP first and then add the new one
                InMemoryOtpCache.put(email, otp);

                emailService.sendOtp(email, otp);
            } else {
                throw new SocialException(String.format("Email: %s already exists!", userDto.getEmail()));
            }
        } catch (SocialException e) {
            throw new SocialException(String.format("Register failed for email: %s due to %s", userDto.getEmail(), e.getMessage()), e);
        }
    }

    public void verify(UserDto userDto) throws SocialException {
        try {
            String email = userDto.getEmail();
            String otp = userDto.getOtp();

            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                Otp storedOtp = InMemoryOtpCache.get(email);
                LocalDateTime now = LocalDateTime.now();
                String storedOtpValue = storedOtp.getValue();
                LocalDateTime storedOtpExpiration = storedOtp.getExpiration();

                if (!otp.equals(storedOtpValue))
                    throw new SocialException("OTP does not match!");
                // TODO: Use constant value for minutes
                if (now.minusMinutes(10).isAfter(storedOtpExpiration))
                    throw new SocialException("OTP expired!");

                User user = optionalUser.get();
                user.setVerified(true);
                userRepository.save(user);

                // TODO: Authenticate and authorize
                // Return jwt
            } else {
                throw new SocialException(String.format("User not found for email: %s", email));
            }
        } catch (SocialException e) {
            throw new SocialException(String.format("Verify failed for email: %s due to %s", userDto.getEmail(), e.getMessage()), e);
        }
    }
}