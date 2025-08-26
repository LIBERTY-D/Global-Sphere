package com.daniel.app.global.sphere.models;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    private Long id;
    private Long authorId;
    private String title;
    private String description;
    private String content;
    private String imageUrl;
    private String externalUrl;
    private String author;
    private LocalDateTime publishedAt;
}