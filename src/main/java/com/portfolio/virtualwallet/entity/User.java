package com.portfolio.virtualwallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols")
    private String username;

    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[+\\-*&^]).{8,}$",
            message = "Password must be at least 8 characters and contain a capital letter," +
                    " a digit, and a special symbol (+, -, *, &, ^).")
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "Email must be valid.")
    private String email;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;

    @Column
    private String photoUrl;

    @Column(nullable = false)
    private boolean isBlocked;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private Set<Card> cards = new HashSet<>();

}
