package com.backend.features.authentication.controller;

import com.backend.features.authentication.dto.AuthenticationRequestBody;
import com.backend.features.authentication.dto.AuthenticationResponseBody;
import com.backend.features.authentication.model.AuthenticationUser;
import com.backend.features.authentication.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationUserService;

    @GetMapping("/")
    public String hello(){
        return "Hello dude";
    }

    @PostMapping("/login")
    public AuthenticationResponseBody loginPage(@Valid @RequestBody AuthenticationRequestBody loginRequestBody) {
        return authenticationUserService.login(loginRequestBody);
    }

    @PostMapping("/register")
    public AuthenticationResponseBody registerPage(@Valid @RequestBody AuthenticationRequestBody registerRequestBody) throws MessagingException, UnsupportedEncodingException {
        return authenticationUserService.register(registerRequestBody);
    }


    @PutMapping("/validate-email-verification-token")
    public String verifyEmail(@RequestParam String token, @RequestAttribute("authenticatedUser") AuthenticationUser user) {
        authenticationUserService.validateEmailVerification(token, user.getEmail());
        return "Email verified";
    }

    @GetMapping("/send-email-verification-token")
    public String  sendEmailVerificationToken(@RequestAttribute("authenticatedUser")AuthenticationUser user) {
        authenticationUserService.sendEmailVerificationToken(user.getEmail());
        return "Email verification token sent successfully.";
    }

    @PutMapping("/send-password-reset-token")
    public String sendPasswordResetToken(@RequestParam String email) {
        authenticationUserService.sendPasswordResetToken(email);
        return "Password reset token sent successfully.";
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String newPassword, @RequestParam String token,
                                  @RequestParam String email) {
        authenticationUserService.resetPassword(email, newPassword, token);
        return "Password reset succ";
    }
}
