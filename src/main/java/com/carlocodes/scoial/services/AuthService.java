package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.AuthDto;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.enums.OtpEnum;
import com.carlocodes.scoial.exceptions.SocialException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserService userService,
                       OtpService otpService,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       TokenService tokenService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public void register(AuthDto authDto) throws SocialException {
        try {
            String email = authDto.getEmail();
            String password = otpService.generateOtp();
            LocalDateTime now = LocalDateTime.now();

            Optional<User> optionalUser = userService.findByEmail(email);

            User user = optionalUser.orElse(new User());

            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setDateTime(now);

            userService.save(user);

            emailService.sendOtp(email, password);
        } catch (SocialException e) {
            throw new SocialException(String.format("Register user with email: %s failed due to %s", authDto.getEmail(), e.getMessage()), e);
        }
    }

    public String verify(AuthDto authDto) throws SocialException {
        try {
            String email = authDto.getEmail();
            String password = authDto.getPassword();

            Optional<User> optionalUser = userService.findByEmail(email);
            User user = optionalUser.orElseThrow(() -> new SocialException(String.format("User with email: %s does not exist!", email)));

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationTime = user.getDateTime().plusMinutes(OtpEnum.OTP_EXPIRATION_MINUTES.getValue());

            if (now.isAfter(expirationTime)) {
                throw new SocialException("OTP expired!");
            }

            if (!user.isVerified()) {
                user.setVerified(true);
                userService.save(user);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            return tokenService.generateToken(user);
        } catch (SocialException e) {
            throw new SocialException(String.format("Verify user with email: %s failed due to %s", authDto.getEmail(), e.getMessage()), e);
        }
    }
}
