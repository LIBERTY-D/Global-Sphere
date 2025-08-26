package com.daniel.app.global.sphere.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String author;
    private String text;
    private  String avatar;
    private LocalDateTime createdAt;

    // Optional: derived field for Thymeleaf
    public String getTimeAgo() {
        return createdAt == null ? "just now" : createdAt.toString();
    }
}
