package com.daniel.app.global.sphere.dtos;


import com.daniel.app.global.sphere.annotation.MatchFieldValueAnnotation;
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
@MatchFieldValueAnnotation.Fields({
        @MatchFieldValueAnnotation(message = "passwords do not match",
                field ="password", fieldMatch = "confirmPassword")
})
public class SignUp {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Confirm Password is required")
    @Size(min = 6, message = "Confirm Password must be at least 6 characters " +
            "long")
    private String confirmPassword;
}

