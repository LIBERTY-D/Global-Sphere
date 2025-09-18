package com.daniel.app.global.sphere.dtos;


import com.daniel.app.global.sphere.annotation.MatchFieldValueAnnotation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@MatchFieldValueAnnotation.Fields({
        @MatchFieldValueAnnotation(message = "passwords do not match",
                field = "password", fieldMatch = "confirmPassword")
})
public class ResetPasswordDto {
    private String token;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull(message = "Password is required")
    private String password;
    @Size(min = 6, message = "ConfirmPassword must be at least 6 characters " + "long")
    @NotNull(message = "confirmPassword is required")
    private String confirmPassword;
}
