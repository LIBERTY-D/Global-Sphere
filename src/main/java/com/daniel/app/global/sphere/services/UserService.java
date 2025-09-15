package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.annotation.LogAspectAnnotation;
import com.daniel.app.global.sphere.config.CustomUserDetailsService;
import com.daniel.app.global.sphere.dtos.EditProfileDto;
import com.daniel.app.global.sphere.dtos.Person;
import com.daniel.app.global.sphere.dtos.SignIn;
import com.daniel.app.global.sphere.dtos.SignUp;
import com.daniel.app.global.sphere.exceptions.AuthException;
import com.daniel.app.global.sphere.mapper.UserMapper;
import com.daniel.app.global.sphere.models.Role;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            User principal = (User) authentication.getPrincipal();
            return findUserByEmail(principal.getEmail());

        }
        return null;
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


    public User findUserImageById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
