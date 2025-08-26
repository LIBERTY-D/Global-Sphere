package com.daniel.app.global.sphere.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUp {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull(message = "Password is required")
    private String password;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull(message = "Password is required")
    private String confirmPassword;
}

