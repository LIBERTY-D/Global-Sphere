package com.daniel.app.global.sphere.models;


import lombok.*;
import java.time.LocalDateTime;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedItem {
    private Long id;
    private String author;
    private String role;
    private String avatar;
    private String content;
    private String codeSnippet;
    private String link;        // optional URL
    private String filePath;    // path/URL of uploaded file
    private LocalDateTime createdAt;
    private int likes;
    private List<Comment> comments;
    private FeedItemType type;
    // Derived field for Thymeleaf: "time ago"
    public String getTimeAgo() {
        return createdAt == null ? "just now" : createdAt.toString();
    }
}

