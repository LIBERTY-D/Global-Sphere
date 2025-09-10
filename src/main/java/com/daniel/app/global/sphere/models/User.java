package com.daniel.app.global.sphere.models;


import com.daniel.app.global.sphere.Utils.RoleUtil;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "comments")
public class User extends BaseEntity implements UserDetails {

    // Basic info
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String bio;
    private byte[] avatar;
    private String occupation;
    private String jobTitle;
    private String password;

    // Role
    private Role role;

    // Social stats
    private int followersCount;
    private int followingCount;
    private int postsCount;

    // Relations
    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    // Posts and comments

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedItem> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Resource> resources = new ArrayList<>();

    private String linkedInUrl;
    private String githubUrl;

    public User(String name, String bio, byte[] avatar,
                String occupation, Role role) {
        this.name = name;
        this.bio = bio;
        this.avatar = avatar;
        this.occupation = occupation;
        this.role = role;
        this.followersCount = 0;
        this.followingCount = 0;
        this.postsCount = 0;
    }

    // === Domain methods ===

    /**
     * Update profile information.
     * Null or blank values are ignored to allow partial updates.
     */
    public void updateUser(String name,
                           String bio,
                           byte[] avatar,
                           String occupation,
                           String jobTitle,
                           String linkedInUrl,
                           String githubUrl) {
        if (name != null && !name.isBlank()) this.name = name;
        if (bio != null) this.bio = bio;
        if (avatar != null) this.avatar = avatar;
        if (occupation != null) this.occupation = occupation;
        if (jobTitle != null) this.jobTitle = jobTitle;
        if (linkedInUrl != null) this.linkedInUrl = linkedInUrl;
        if (githubUrl != null) this.githubUrl = githubUrl;
    }

    // === Follow / Unfollow helpers ===

    public void follow(User userToFollow) {
        if (this.equals(userToFollow)) return;
        if (this.following.add(userToFollow)) {
            this.followingCount = this.following.size();
            userToFollow.followers.add(this);
            userToFollow.followersCount = userToFollow.followers.size();
        }
    }

    public void unfollow(User userToUnfollow) {
        if (this.following.remove(userToUnfollow)) {
            this.followingCount = this.following.size();
            userToUnfollow.followers.remove(this);
            userToUnfollow.followersCount = userToUnfollow.followers.size();
        }
    }

    public void incrementPostsCount() {
        this.postsCount = this.postsCount + 1;
    }


    public void decrementPostsCount() {
        if (this.postsCount > 0) {
            this.postsCount = this.postsCount - 1;
        }
    }

    // === Security ===
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleUtil.getRoles(this.role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // equals & hashCode only on id (important for sets!)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
