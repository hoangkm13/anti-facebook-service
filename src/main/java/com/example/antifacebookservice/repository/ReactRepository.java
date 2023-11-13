package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.React;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactRepository extends MongoRepository<React, String> {
    Optional<React> findByPostIdAndUserId(String postId, String userId);
}
