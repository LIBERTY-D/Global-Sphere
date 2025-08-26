package com.daniel.app.global.sphere.dtos;

import com.daniel.app.global.sphere.models.User;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateDiscussion {

    private User author;

    @NotBlank(message = "Discussion text cannot be empty")
    @Size(max = 2000, message = "Discussion text cannot exceed 2000 characters")
    private String text;

    private LocalDateTime createdAt;
}
