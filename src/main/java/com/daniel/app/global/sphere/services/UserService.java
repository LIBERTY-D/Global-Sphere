package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.annotation.LogAspectAnnotation;
import com.daniel.app.global.sphere.config.CustomUserDetailsService;
import com.daniel.app.global.sphere.dtos.*;
import com.daniel.app.global.sphere.exceptions.AuthException;
import com.daniel.app.global.sphere.mapper.UserMapper;
import com.daniel.app.global.sphere.models.Role;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User authenticate(SignIn signIn) throws AuthException {
        User user;
        try {
            user = (User) customUserDetailsService.loadUserByUsername(signIn.getEmail());
        } catch (UsernameNotFoundException e) {
            throw new AuthException("email", "Email not found");
        }

        if (!passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
            throw new AuthException("password", "Incorrect password");
        }

        return user;
    }

    public byte[] getUserImage() {
        return getAuthenticatedUser().getAvatar();
    }

    @LogAspectAnnotation
    public boolean registerUser(SignUp signUp) {
        log.info("CREATING USER");
        User createUser = UserMapper.toUser(signUp);
        createUser.setPassword(passwordEncoder.encode(signUp.getPassword()));
        createUser.setRole(Role.USER);
        userRepository.save(createUser);
        return true;
    }

    @LogAspectAnnotation
    public boolean updateUser(EditProfileDto editProfileDto) throws IOException {
        User user = getAuthenticatedUser();
        User updatedUser = UserMapper.toUser(user, editProfileDto);
        userRepository.save(updatedUser);
        return true;
    }

    public User findUserByEmail(String email) {
        return findByEmail(email);
    }

    private User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);

    }


    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        User principal = (User) authentication.getPrincipal();
        return userRepository.findByEmail(principal.getEmail()).orElse(null);
    }


    public List<Person> peopleToFollow() {
        User authenticated = getAuthenticatedUser();
        if (authenticated != null) {
            return getAllUsers().stream()
                    .filter(u -> !Objects.equals(u.getId(), authenticated.getId()))
                    .filter(u -> !authenticated.getFollowing().contains(u))
                    .map(u -> new Person(
                            u.getName(),
                            u.getId(),
                            u.getAvatar(),
                            u.getJobTitle(),
                            u.getOccupation()
                    ))
                    .toList();
        }
        return List.of();
    }

    @Transactional
    public boolean toggleFollow(Long userId) {
        User principalUser = getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(principalUser.getEmail()).orElseThrow();
        User userToFollow = userRepository.findById(userId).orElseThrow();

        if (currentUser.getFollowing().contains(userToFollow)) {
            currentUser.unfollow(userToFollow);
            return false;
        } else {
            currentUser.follow(userToFollow);
            return true;
        }
    }

    @Transactional
    public void followUser(Long userId) {
        User principalUser = getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(principalUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        User userToFollow = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User to follow not found"));
        if (!currentUser.getFollowing().contains(userToFollow)) {
            currentUser.follow(userToFollow);
        }
    }

    @Transactional
    public void unfollowUser(Long userId) {
        User principalUser = getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(principalUser.getEmail()).orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        User userToUnfollow = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User to unfollow not found"));
        if (currentUser.getFollowing().contains(userToUnfollow)) {
            currentUser.unfollow(userToUnfollow);
        }
    }

    @Transactional
    public void removeFollower(Long followerId) {
        User principalUser = getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(principalUser.getEmail()).orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        if (currentUser.getFollowers().contains(follower)) {
            currentUser.unfollow(follower);
            follower.unfollow(currentUser);

        }
    }


    public User findUserImageById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    // called by admin
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).get();
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            if (principal instanceof UserDetails userDetails && userDetails.getUsername().equals(user.getEmail())) {
                sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);
            }
        });
        userRepository.deleteById(id);
    }

    public void ensureAnonymousIfDeleted(User user) {
        if (user == null) {
            SecurityContextHolder.getContext().setAuthentication(
                    new AnonymousAuthenticationToken(
                            "anonymous",
                            "anonymousUser",
                            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                    )
            );
        }
    }

    public boolean passwordsMatch(String email, String currentPassword) {
        User user = findByEmail(email);
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public void updatePassword(String name, UpdatePasswordDto form) {
        User user = findByEmail(name);
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserByEmail(String name) {
        User user = findByEmail(name);
        userRepository.delete(user);
    }

    public void updateUser(UpdateUserProfileAdmin dto) throws IOException {
        User user = userRepository.findById(dto.getId()).get();
        User updatedUser = UserMapper.toUser(user, dto);
        userRepository.save(updatedUser);

    }

    public UpdateUserProfileAdmin findUserById(Long id) {
        User user = userRepository.findById(id).orElse(new User());
        UpdateUserProfileAdmin dto = new UpdateUserProfileAdmin();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole());
        dto.setJobTitle(dto.getJobTitle());
        dto.setAvatar(null);
        return dto;

    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
