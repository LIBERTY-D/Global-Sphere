package com.daniel.app.global.sphere.dtos;


import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateComment {

    private Long postId;

    private String author;

    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String text;

    private LocalDateTime createdAt;
}
