package com.daniel.app.global.sphere.models;

import java.time.LocalDateTime;
import java.util.List;


public class Discussion {
    private Long id;
    private User author;
    private String text;
    private LocalDateTime createdAt;
    private List<Comment> comments;
}
