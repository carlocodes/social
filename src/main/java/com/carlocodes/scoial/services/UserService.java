package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.Otp;
import com.carlocodes.scoial.dtos.UserDto;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

                emailService.sendOtp(email, otp);
            } else {
                throw new SocialException(String.format("Email: %s already exists!", userDto.getEmail()));
            }
        } catch (SocialException e) {
            throw new SocialException(String.format("Register failed for email: %s due to %s", userDto.getEmail(), e.getMessage()), e);
        }
    }
}
