package com.daniel.app.global.sphere.dtos;


import com.daniel.app.global.sphere.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "avatar")
public class UpdateUserProfileAdmin {

    private  Long id;
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;

    @Size(max = 250, message = "Bio must not exceed 250 characters")
    private String bio;

    @Size(max = 50, message = "Job title must not exceed 50 characters")
    private String jobTitle;

    private Role role;

    private MultipartFile avatar;
}
