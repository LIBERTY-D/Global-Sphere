package com.daniel.app.global.sphere.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class SignIn {
    @Email(message = "Please provide a valid email")
    @NotNull(message = "Email is required")
    private  String email;

    @NotBlank(message = "Password is required")
    private  String password;
}
