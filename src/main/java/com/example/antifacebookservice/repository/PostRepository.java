package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findTopByIdAndCategoryIdOrderByCreatedAtDesc(String id, String categoryId);

    int countAllByCreatedAtAfter(String createdAt);

    @Query("{'described': {'$regex': ?0, '$options': 'i'}}")
    List<Post> searchPostByDescribedLike(String keyword);
}
