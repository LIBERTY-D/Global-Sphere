package com.daniel.app.global.sphere.mapper;

import com.daniel.app.global.sphere.dtos.EditProfileDto;
import com.daniel.app.global.sphere.dtos.SignUp;
import com.daniel.app.global.sphere.models.Comment;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.User;


public class UserMapper {

    public static User toUser(User dbUser, EditProfileDto editProfileDto) {
        // TODO: handle avatar image properly later
        dbUser.setName(editProfileDto.getName());
        dbUser.setOccupation(editProfileDto.getOccupation());
        dbUser.setJobTitle(editProfileDto.getJobTitle());
        dbUser.setBio(editProfileDto.getBio());
        for (FeedItem feed : dbUser.getFeeds()) {
            feed.setAuthor(dbUser.getName());
            feed.setAvatar(dbUser.getAvatar());
            feed.setUser(dbUser);

        }
        for (Comment comment : dbUser.getComments()) {
            comment.setAvatar(dbUser.getAvatar());
            comment.setUser(dbUser);
            comment.setAuthor(dbUser.getName());

        }
        return dbUser;
    }

    public static User toUser(SignUp signUp) {
        var user = new User();
        user.setName(signUp.getUsername());
        user.setEmail(signUp.getEmail());
        user.setPassword(signUp.getPassword());
        return user;
    }
}
