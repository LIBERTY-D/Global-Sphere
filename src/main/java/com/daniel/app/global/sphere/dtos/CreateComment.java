package com.daniel.app.global.sphere.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateComment {
    private Long id;//for updated only
    private Long postId;
    private String author;
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String text;
    private LocalDateTime createdAt;
}
