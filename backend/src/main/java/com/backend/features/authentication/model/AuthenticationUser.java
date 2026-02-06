package com.backend.features.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class AuthenticationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @Email
    @NotNull
    private String email;
    private Boolean emailVerified=false;
    private String emailVerificationToken=null;
    private LocalDateTime emailVerificationTokenExpiryDate=null;

    @JsonIgnore
    private String password;
    private String passwordResetToken =null;
    private LocalDateTime passwordResetTokenExpiryDate=null;
    // opzionale: costruttore con solo email e password
    public AuthenticationUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
