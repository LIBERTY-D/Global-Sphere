package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.dtos.SignIn;
import com.daniel.app.global.sphere.dtos.SignUp;
import com.daniel.app.global.sphere.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    public User loginUser(SignIn signIn){
        User currentUser = new User(
                "Alex Doe",
                "Computer Science • Spring MVC",
                null,
                "Instructor",
                "Instructor"
        );
        currentUser.setFollowersCount(120);
        currentUser.setFollowingCount(75);
        currentUser.setPostsCount(10);
        currentUser.setEmail("alex.doe@example.com");
        currentUser.setLinkedInUrl("https://linkedin.com/in/alexdoe");
        currentUser.setGithubUrl("https://github.com/alexdoe");
        currentUser.setId(1L);
        return  currentUser;
    }

    public boolean registerUser(SignUp signUp){

        return true;
    }

    public User getAuthenticatedUser(){
        User currentUser = new User(
                "Alex Doe",
                "Computer Science • Spring MVC",
                "https://i.pravatar.cc/96?img=5",
                "Instructor",
                "Instructor"
        );
        currentUser.setFollowersCount(120);
        currentUser.setFollowingCount(75);
        currentUser.setPostsCount(10);
        currentUser.setEmail("alex.doe@example.com");
        currentUser.setLinkedInUrl("https://linkedin.com/in/alexdoe");
        currentUser.setGithubUrl("https://github.com/alexdoe");
        currentUser.setId(1L);
        return  currentUser;
    }
}
