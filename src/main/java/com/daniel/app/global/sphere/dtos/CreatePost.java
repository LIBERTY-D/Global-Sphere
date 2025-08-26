package com.daniel.app.global.sphere.dtos;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "file")
public class CreatePost {

    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 2000, message = "Post content cannot exceed 2000 characters")
    private String content;

    @Size(max = 1000, message = "Code snippet cannot exceed 1000 characters")
    private String codeSnippet;

    @Size(max = 500, message = "Link cannot exceed 500 characters")
    private String link;

    private MultipartFile file;

    // Hidden fields filled from backend
    private String author;
    private String role;
    private String avatar;
}
