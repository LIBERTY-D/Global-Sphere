package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.repository.CommentRepository;
import com.daniel.app.global.sphere.repository.FeedRepository;
import com.daniel.app.global.sphere.repository.ResourceRepository;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ResourceRepository resourceRepository;
    private  final CommentRepository commentRepository;

    public Integer resourceCount() {
        return resourceRepository.findAll().size();
    }


    public Integer commentCount() {
        return commentRepository.findAll().size();
    }

    public Integer feedCount() {
        return feedRepository.findAll().size();
    }


    public Integer userCount() {
        return userRepository.findAll().size();
    }
}
