package com.daniel.app.global.sphere.models;


import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = "user")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String author;
    private String text;
    private  byte [] avatar;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private  User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id",nullable = true)
    private  FeedItem feed;

    public String getTimeAgo() {
        return this.getCreatedAt() == null ? "just now" :
                this.getCreatedAt().toString();
    }
}
