package com.daniel.app.global.sphere.models;


import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feed")
public class FeedItem extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private Role role;
    private String avatar;
    private String content;
    private String codeSnippet;
    private String link;
    private byte[] filePath;
    private int likes;

    @ElementCollection
    @CollectionTable(
            name = "feed_likes",
            joinColumns = @JoinColumn(name = "feed_id")
    )
    @Column(name = "user_id")
    private List<Long> likesIds = new ArrayList<>();


    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private  User user;
    private FeedItemType type;

    public String getTimeAgo() {
        return this.getCreatedAt() == null ? "just now" : this.getCreatedAt().toString();
    }

    public  void incrementLikes(){
        this.likes = this.likes+1;
    }
    public  void descrementLikes(){
       if(this.likes>0){
           this.likes =  this.likes-1;
       }
    }
}

