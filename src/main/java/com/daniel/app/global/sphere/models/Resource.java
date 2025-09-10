package com.daniel.app.global.sphere.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Resource extends  BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private  Long authorId;
    private String title;
    private String description;
    private String content;
    private byte[] imageUrl;
    private String externalUrl;
    private String author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private  User user;
}