package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findAllByPostId(String postId);
}
