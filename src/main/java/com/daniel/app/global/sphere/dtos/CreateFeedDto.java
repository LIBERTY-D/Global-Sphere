package com.daniel.app.global.sphere.dtos;


import com.daniel.app.global.sphere.annotation.NotEmptyFile;
import com.daniel.app.global.sphere.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "file")
public class CreateFeedDto {

    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 2000, message = "Post content cannot exceed 2000 characters")
    private String content;

    @Size(max = 1000, message = "Code snippet cannot exceed 1000 characters")
    private String codeSnippet;

    @Size(max = 500, message = "Link cannot exceed 500 characters")
    private String link;

    @NotEmptyFile()
    private MultipartFile file;

    // Hidden fields filled from backend
    private String author;
    private Role role;
    private byte[] avatar;
}
