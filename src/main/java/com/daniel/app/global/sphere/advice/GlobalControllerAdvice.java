package com.daniel.app.global.sphere.advice;

import com.daniel.app.global.sphere.dtos.*;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute("signinForm")
    public SignIn signinForm() {
        return new SignIn();
    }

    @ModelAttribute("forgotForm")
    public ForgotPassword forgotForm() {
        return new ForgotPassword();
    }

    @ModelAttribute("signUpForm")
    public SignUp signUpForm() {
        return new SignUp();
    }

    @ModelAttribute("editProfileForm")
    public EditProfileDto editProfileForm() {
        User currentUser = userService.getAuthenticatedUser();
        return currentUser != null
                ? new EditProfileDto(currentUser.getName(), currentUser.getBio(),
                currentUser.getJobTitle(), currentUser.getOccupation(), null)
                : new EditProfileDto();
    }

    @ModelAttribute("createPostForm")
    public CreatePost createPostForm() {
        User currentUser = userService.getAuthenticatedUser();
        CreatePost post = new CreatePost();
        if (currentUser != null) {
            post.setAuthor(currentUser.getName());
            post.setRole(currentUser.getRole());
            post.setAvatar(currentUser.getAvatar());
        }
        return post;
    }

    @ModelAttribute("createCommentForm")
    public CreateComment createCommentForm() {
        User currentUser = userService.getAuthenticatedUser();
        CreateComment comment = new CreateComment();
        if (currentUser != null) {
            comment.setAuthor(currentUser.getName());
            comment.setCreatedAt(LocalDateTime.now());
        }
        return comment;
    }
    @ModelAttribute("createDiscussionForm")
    public CreateDiscussion createDiscussionForm() {
        User currentUser = userService.getAuthenticatedUser();
        CreateDiscussion discussion = new CreateDiscussion();
        if (currentUser != null) {
            discussion.setAuthor(currentUser);
            discussion.setCreatedAt(LocalDateTime.now());
        }
        return discussion;
    }

}
