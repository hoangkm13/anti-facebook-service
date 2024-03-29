package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.constant.FeelType;
import com.example.antifacebookservice.entity.React;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactRepository extends MongoRepository<React, String> {
    Optional<React> findByPostIdAndUserId(String postId, String userId);

    Integer countAllByFeelTypeAndPostId(FeelType feelType, String id);

    List<React> findByPostId(String postId);
}
