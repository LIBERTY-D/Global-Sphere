package com.daniel.app.global.sphere.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedDto  {

    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 2000, message = "Post content cannot exceed 2000 characters")
    private String content;

    @Size(max = 1000, message = "Code snippet cannot exceed 1000 characters")
    private String codeSnippet;

    @Size(max = 500, message = "Link cannot exceed 500 characters")
    private String link;
}
