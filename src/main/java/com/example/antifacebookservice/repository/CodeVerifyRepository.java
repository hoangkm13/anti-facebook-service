package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.CodeVerify;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeVerifyRepository extends MongoRepository<CodeVerify, String> {
    Optional<CodeVerify> findCodeVerifiesByUsername(String userName);
}
