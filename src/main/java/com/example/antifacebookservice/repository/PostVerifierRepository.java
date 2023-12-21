package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.PostVerifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVerifierRepository extends MongoRepository<PostVerifier, String> {
    Integer countAllByPostIdAndIsTrust(String postId, Boolean isTrust);

    Boolean existsByPostIdAndUserIdAndIsTrust(String postId, String userId, Boolean isTrust);
}
