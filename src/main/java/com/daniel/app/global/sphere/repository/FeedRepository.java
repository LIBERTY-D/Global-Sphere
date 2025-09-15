package com.daniel.app.global.sphere.repository;

import com.daniel.app.global.sphere.models.FeedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedRepository extends JpaRepository<FeedItem, Long> {
    @Query("SELECT f FROM FeedItem f WHERE LOWER(f.author) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY f.createdAt DESC")
    List<FeedItem> searchByAuthorOrContent(@Param("query") String query);

}
