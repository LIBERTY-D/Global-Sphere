package com.daniel.app.global.sphere.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ForgotPassword {
    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private  String email;
}
