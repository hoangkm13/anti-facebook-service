package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.DevToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevTokenRepository extends MongoRepository<DevToken, String> {
}
