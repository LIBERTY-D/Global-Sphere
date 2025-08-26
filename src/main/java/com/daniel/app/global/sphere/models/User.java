package com.daniel.app.global.sphere.models;


import com.daniel.app.global.sphere.dtos.CreatePost;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    // Basic info
    private Long id;                     // unique identifier
    private String name;                 // e.g., "Alex Doe"
    private String bio;                  // short bio
    private String avatar;            // URL to profile picture
    private String occupation;           // e.g., "Computer Science • Spring MVC"
    private String jobTitle;
    // Role
    private String role;                 // "Student" or "Instructor"

    // Social stats
    private int followersCount;
    private int followingCount;
    private int postsCount;

    // Relations
    private List<Long> followers;        // List of user IDs who follow this user
    private List<Long> following;        // List of user IDs this user follows

    // Posts and comments
    private List<CreatePost> posts;            // Posts created by this user
    private List<Comment> comments;      // Comments made by this user

    // Optional: contact info or social links
    private String email;
    private String linkedInUrl;
    private String githubUrl;

    // Constructor for creating a new user without relations
    public User(String name, String bio, String avatar, String occupation, String role) {
        this.name = name;
        this.bio = bio;
        this.avatar = avatar;
        this.occupation = occupation;
        this.role = role;
        this.followersCount = 0;
        this.followingCount = 0;
        this.postsCount = 0;
    }
}

