package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.ReportPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostRepository extends MongoRepository<ReportPost, String> {

    boolean existsByPostIdAndUserId(String postId, String userId);
}
