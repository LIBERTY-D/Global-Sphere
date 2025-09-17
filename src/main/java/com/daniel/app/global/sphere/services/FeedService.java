package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.Utils.CreatedAtComparator;
import com.daniel.app.global.sphere.annotation.LogAspectAnnotation;
import com.daniel.app.global.sphere.dtos.CreateComment;
import com.daniel.app.global.sphere.dtos.CreateDiscussion;
import com.daniel.app.global.sphere.dtos.CreateFeedDto;
import com.daniel.app.global.sphere.dtos.UpdateFeedDto;
import com.daniel.app.global.sphere.exceptions.DataIntegrityException;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
import com.daniel.app.global.sphere.mapper.FeedMapper;
import com.daniel.app.global.sphere.models.Comment;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.ImageEntity;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.FeedRepository;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public boolean createFeed(CreateFeedDto createFeedDto) {
        log.info("CREATING FEED");

        try {
            User user = userService.getAuthenticatedUser();
            user.incrementPostsCount();
            FeedItem feedItem = FeedMapper.toFeedItem(user, createFeedDto);
            if (createFeedDto.getFile() != null && !createFeedDto.getFile().isEmpty()) {
                MultipartFile file = createFeedDto.getFile();
                ImageEntity image = ImageEntity.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .data(file.getBytes())
                        .build();

                feedItem.getImages().add(image);
            }

            feedItem.setUser(user);
            List<FeedItem> feedItems = user.getFeeds();
            feedItems.add(feedItem);
            user.setFeeds(feedItems);

            userRepository.save(user);
            return true;
        } catch (IOException | DataIntegrityViolationException exp) {
            if (exp instanceof DataIntegrityViolationException) {
                throw new DataIntegrityException(((DataIntegrityViolationException) exp).
                        getMostSpecificCause().getMessage(), createFeedDto);
            }
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
    public FeedItem updateFeed(Long postId, UpdateFeedDto updateFeedDto) throws IOException {
        FeedItem feed = feedRepository.findById(postId).orElse(new FeedItem());
        feed.setContent(updateFeedDto.getContent());
        feed.setCodeSnippet(updateFeedDto.getCodeSnippet());
        feed.setLink(updateFeedDto.getLink());
        feed.getImages().clear();
        MultipartFile file = updateFeedDto.getFile();
        ImageEntity image = ImageEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build();
        feed.getImages().add(image);
        return feed;
    }

    @Transactional
    @LogAspectAnnotation
    public void deletePost(Long postId, User currentUser) {
        FeedItem feed = feedRepository.findById(postId).orElse(new FeedItem());
        feed.getUser().decrementPostsCount();
        feedRepository.delete(feed);
    }

    public FeedItem getFeedById(Long id) {
        return feedRepository.findById(id).orElseThrow(() -> new RuntimeException("Feed not found"));
    }

    @Transactional
    public FeedItem toggleLikeFeed(Long id) {
        FeedItem feedItem = feedRepository.findById(id).orElse(new FeedItem());
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


    public void addImageToFeed(Long feedId, MultipartFile file) throws IOException {
        FeedItem feed = getFeedById(feedId);
        ImageEntity image = ImageEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build();
        feed.getImages().add(image);
        feedRepository.save(feed);
    }


    public List<FeedItem> searchFeeds(String query) {
        return feedRepository.searchByAuthorOrContent(query);
    }

    @Transactional
    public void updateFeed(UpdateFeedDto updateFeedDto) throws IOException {
        FeedItem feed = feedRepository.findById(updateFeedDto.getId()).orElse(new FeedItem());
        feed.setContent(updateFeedDto.getContent());
        feed.setCodeSnippet(updateFeedDto.getCodeSnippet());
        feed.setLink(updateFeedDto.getLink());
        feed.getImages().clear();
        MultipartFile file = updateFeedDto.getFile();
        ImageEntity image = ImageEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build();
        feed.getImages().add(image);

    }
}
