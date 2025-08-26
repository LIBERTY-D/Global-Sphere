package com.daniel.app.global.sphere.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "avatar")
public class EditProfileDto {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;

    @Size(max = 250, message = "Bio must not exceed 250 characters")
    private String bio;

    @Size(max = 50, message = "Job title must not exceed 50 characters")
    private String jobTitle;

    @NotBlank(message = "Occupation is required")
    @Pattern(
            regexp = "Student|Instructor|Mentor|Professional|Other",
            message = "Invalid occupation"
    )
    private String occupation;

    private MultipartFile avatar;
}
