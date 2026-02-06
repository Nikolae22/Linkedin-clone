package com.backend.features.authentication.service;

import com.backend.features.authentication.dto.AuthenticationRequestBody;
import com.backend.features.authentication.dto.AuthenticationResponseBody;
import com.backend.features.authentication.model.AuthenticationUser;
import com.backend.features.authentication.repository.AuthenticationUserRepository;
import com.backend.features.authentication.utility.EmailService;
import com.backend.features.authentication.utility.Encoder;
import com.backend.features.authentication.utility.JsonWebToken;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationUserRepository authenticationUserRepository;
    private final Encoder encoder;
    private final JsonWebToken jsonWebToken;
    private final EmailService emailService;
    private final int durationInMinutes = 1;


    public AuthenticationUser getUser(String email) {
        return authenticationUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    public static String generateEmailVerificationToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            token.append(random.nextInt(10)); //appending random digix from 0 to 9
        }
        return token.toString();
    }

    public void sendEmailVerificationToken(String email) {
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
        if (user.isPresent() && !user.get().getEmailVerified()) {
            String emailVerificationToken = generateEmailVerificationToken();
            String hashedToken = encoder.encode(emailVerificationToken);
            user.get().setEmailVerificationToken(hashedToken);
            user.get().setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            authenticationUserRepository.save(user.get());
            String subject = "Email verification";
            String body = String.format("Only one step to take full advantages. \n\n"
                            + "Enter this code to verify your mail " + "%\n\n"
                            + "The code will expire in " + "%s" + " minutes",
                    emailVerificationToken, durationInMinutes);

            try {
                emailService.sendEmail(email, subject, body);
            } catch (Exception e) {
                log.info("Error while sending mail {}", e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("email verification token failed or mails already verified");
        }
    }

    public void validateEmailVerification(String token, String email) {
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
        if (user.isPresent() && encoder.matches(token, user.get().getEmailVerificationToken())
                && !user.get().getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            user.get().setEmailVerificationToken(null);
            user.get().setEmailVerificationTokenExpiryDate(null);
            authenticationUserRepository.save(user.get());
        } else if (user.isPresent() && encoder.matches(token, user.get().getEmailVerificationToken())
                && user.get().getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Email verification token expired");
        } else {
            throw new IllegalArgumentException("Email verified already");
        }
    }


    public AuthenticationResponseBody register(AuthenticationRequestBody registerRegisterBody) throws MessagingException, UnsupportedEncodingException {
        AuthenticationUser user = authenticationUserRepository.save(new AuthenticationUser(
                registerRegisterBody.getEmail(),
                encoder.encode(registerRegisterBody.getPassword())));

        String emailVerificationToken = generateEmailVerificationToken();
        String hashedToken = encoder.encode(emailVerificationToken);
        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));

        authenticationUserRepository.save(user);

        String subject = "Email Verification";
        String body = String.format("""
                Only one step to take full advantage;
                
                Enter this code to verify your email: %s The code will expire in %s minutes
                """, emailVerificationToken, durationInMinutes);

        try {
            emailService.sendEmail(registerRegisterBody.getEmail(), subject, body);
        } catch (Exception e) {
            log.info("Error while sending email {}", e.getMessage());
        }
        String token = jsonWebToken.generateToken(registerRegisterBody.getEmail());
        return new AuthenticationResponseBody(token, "user register succ");
    }

    public AuthenticationResponseBody login(AuthenticationRequestBody loginRequestBody) {
        AuthenticationUser user = authenticationUserRepository
                .findByEmail(loginRequestBody.getEmail())
                .orElseThrow(
                        () -> new IllegalArgumentException("User not found")
                );
        if (!encoder.matches(loginRequestBody.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        String toke = jsonWebToken.generateToken(loginRequestBody.getEmail());
        return new AuthenticationResponseBody(toke, "Authentication succ");
    }

    public void sendPasswordResetToken(String email) {
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
        if (user.isPresent()) {
            String passwordResetToken = generateEmailVerificationToken();
            String hashedToken = encoder.encode(passwordResetToken);
            user.get().setPasswordResetToken(hashedToken);
            user.get().setPasswordResetTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            authenticationUserRepository.save(user.get());
            String subject = "Password Reset";
            String body = String.format("""
                    You requested a password reset.

                    Enter this code to reset your password: %s. The code will expire in %s minutes.""",
                    passwordResetToken, durationInMinutes);
            try {
                emailService.sendEmail(email, subject, body);
            } catch (Exception e) {
                log.info("Error while sending email: {}", e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }

    public void resetPassword(String email, String newPassword, String token) {
        Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
        if (user.isPresent() && encoder.matches(token, user.get().getPasswordResetToken())
                && !user.get().getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            user.get().setPasswordResetToken(null);
            user.get().setPasswordResetTokenExpiryDate(null);
            user.get().setPassword(encoder.encode(newPassword));
            authenticationUserRepository.save(user.get());
        } else if (user.isPresent() && encoder.matches(token, user.get().getPasswordResetToken())
                && user.get().getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Password reset token expired.");
        } else {
            throw new IllegalArgumentException("Password reset token failed.");
        }
    }
}
