package com.daniel.app.global.sphere.mapper;

import com.daniel.app.global.sphere.dtos.CreateDiscussion;
import com.daniel.app.global.sphere.dtos.CreateFeedDto;
import com.daniel.app.global.sphere.models.FeedItem;
import com.daniel.app.global.sphere.models.FeedItemType;
import com.daniel.app.global.sphere.models.User;

import java.io.IOException;

public class FeedMapper {


    public static FeedItem toFeedItem(User user, CreateFeedDto createPost) throws IOException {
        FeedItem feedItem = new FeedItem();
        feedItem.setAuthor(user.getName());
        feedItem.setAvatar(user.getAvatar());
        feedItem.setCodeSnippet(createPost.getCodeSnippet());
        feedItem.setComments(null);
        feedItem.setType(FeedItemType.POST);
        feedItem.setContent(createPost.getContent());
        feedItem.setLink(createPost.getLink());
        feedItem.setRole(user.getRole());
        return feedItem;
    }

    public static FeedItem toCreateDiscussion(User user, CreateDiscussion createDiscussion) {
        FeedItem feedItem = new FeedItem();
        feedItem.setAuthor(user.getName());
        feedItem.setAvatar(user.getAvatar());
        feedItem.setCodeSnippet(null);
        feedItem.setComments(null);
        feedItem.setType(FeedItemType.DISCUSSION);
        feedItem.setContent(createDiscussion.getText());
        feedItem.setLink(null);
        feedItem.setRole(user.getRole());
        return feedItem;
    }

    public static FeedItem toUpdateDiscussion(User user,
                                              FeedItem feedItem,
                                              CreateDiscussion updateDto) {

        feedItem.setAuthor(user.getName());
        feedItem.setAvatar(user.getAvatar());
        feedItem.setType(FeedItemType.DISCUSSION);
        feedItem.setContent(updateDto.getText());
        feedItem.setRole(user.getRole());
        return feedItem;
    }

}
