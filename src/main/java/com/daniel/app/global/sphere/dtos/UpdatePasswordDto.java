package com.daniel.app.global.sphere.dtos;


import com.daniel.app.global.sphere.annotation.MatchFieldValueAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@MatchFieldValueAnnotation.Fields({
        @MatchFieldValueAnnotation(message = "passwords do not match", field ="newPassword", fieldMatch = "confirmPassword")
})
public class UpdatePasswordDto {
    @NotBlank(message = "Current password is required")
    @Size(min = 6, message = "Current Password must be at least 6 characters " + "long")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;

    @NotBlank(message = "Please confirm your new password")
    private String confirmPassword;
}
