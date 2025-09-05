package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.Utils.CreatedAtComparator;
import com.daniel.app.global.sphere.annotation.LogAspectAnnotation;
import com.daniel.app.global.sphere.dtos.CreateComment;
import com.daniel.app.global.sphere.dtos.CreateDiscussion;
import com.daniel.app.global.sphere.dtos.CreateFeedDto;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
import com.daniel.app.global.sphere.mapper.FeedMapper;
import com.daniel.app.global.sphere.models.Comment;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.FeedRepository;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;


    public List<FeedItem> getFeeds() {
        return this.feedRepository.findAll().stream().sorted(new CreatedAtComparator<>()).toList();
    }

    public List<FeedItem> getAll() {
        return this.feedRepository.findAll().stream().sorted(new CreatedAtComparator<>()).toList();
    }

    @LogAspectAnnotation
    public boolean createPost(CreateFeedDto createFeedDto) throws FileHandlerException {
        log.info("CREATING POST");

        try {
            User user = userService.getAuthenticatedUser();
            user.incrementPostsCount();
            FeedItem feedItem = FeedMapper.toFeedItem(user, createFeedDto);
            feedItem.setUser(user);
            List<FeedItem> feedItems = user.getFeeds();
            feedItems.add(feedItem);
            user.setFeeds(feedItems);
            userRepository.save(user);
            return true;
        } catch (IOException exp) {
            System.out.println(exp.getMessage());
            throw new FileHandlerException("file", exp.getMessage());

        }

    }

    @LogAspectAnnotation
    public boolean createDiscussion(CreateDiscussion createDiscussion) {
        log.info("CREATING DISCUSSION");

        User user = userService.getAuthenticatedUser();
        FeedItem discussion = FeedMapper.toCreateDiscussion(user, createDiscussion);
        discussion.setUser(user);
        List<FeedItem> feedItems = user.getFeeds();
        feedItems.add(discussion);

        user.setFeeds(feedItems);

        userRepository.save(user);
        return true;

    }

    @Transactional
    @LogAspectAnnotation
    public boolean createComment(CreateComment createComment) {
        log.info("CREATING COMMENT");
        User user = userService.getAuthenticatedUser();
        FeedItem feedItem = feedRepository.findById(createComment.getPostId()).get();

        Comment newComment = new Comment();
        newComment.setAvatar(user.getAvatar());
        newComment.setAuthor(user.getName());
        newComment.setText(createComment.getText());
        newComment.setUser(user);
        newComment.setFeed(feedItem);

        List<Comment> comments = user.getComments();
        comments.add(newComment);

        user.setComments(comments);
        userRepository.save(user);

        return true;
    }

    @Transactional
    @LogAspectAnnotation
    public FeedItem updateFeed(Long postId, User currentUser, String content,
                               String codeSnippet, String link) {
        FeedItem post = feedRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own posts");
        }
        post.setContent(content);
        post.setCodeSnippet(codeSnippet);
        post.setLink(link);
        return post;
    }

    @Transactional
    @LogAspectAnnotation
    public void deletePost(Long postId, User currentUser) {
        FeedItem post = feedRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own posts");
        }
        post.getUser().decrementPostsCount();
        feedRepository.delete(post);
    }

    public FeedItem getPostById(Long id) {
        return feedRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public FeedItem toggleLikeFeed(Long id) {
        FeedItem feedItem = feedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such feed to toggle"));

        User currentUser = userService.getAuthenticatedUser();
        Long userId = currentUser.getId();

        if (feedItem.getLikesIds().contains(userId)) {
            feedItem.descrementLikes();
            feedItem.getLikesIds().remove(userId);
        } else {
            feedItem.incrementLikes();
            feedItem.getLikesIds().add(userId);
        }

        return feedRepository.save(feedItem);
    }


}
