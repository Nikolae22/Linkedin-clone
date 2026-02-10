package com.backend.features.authentication.model;

import com.backend.features.feed.modal.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private Boolean emailVerified = false;
    private String emailVerificationToken = null;
    private LocalDateTime emailVerificationTokenExpiryDate = null;

    private String firstName;
    private String lastName;
    private String company;
    private String position;
    private String location;

    private Boolean profileComplete = false;
    private String profilePicture=null;

    @JsonIgnore
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,orphanRemoval = true
    )
    private List<Post> posts;

    @JsonIgnore
    private String password;
    private String passwordResetToken = null;
    private LocalDateTime passwordResetTokenExpiryDate = null;

    // opzionale: costruttore con solo email e password
    public AuthenticationUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private void updateProfileCompletionStatus() {
        this.profileComplete = (
                this.firstName != null &&
                        this.lastName != null &&
                        this.company != null &&
                        this.position != null &&
                        this.location != null
        );
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateProfileCompletionStatus();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateProfileCompletionStatus();
    }

    public void setCompany(String company) {
        this.company = company;
        updateProfileCompletionStatus();
    }

    public void setPosition(String position) {
        this.position = position;
        updateProfileCompletionStatus();
    }

    public void setLocation(String location) {
        this.location = location;
        updateProfileCompletionStatus();
    }
}
