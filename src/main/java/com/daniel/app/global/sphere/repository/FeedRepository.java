package com.daniel.app.global.sphere.repository;

import com.daniel.app.global.sphere.models.FeedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedRepository extends JpaRepository<FeedItem, Long> {
}
