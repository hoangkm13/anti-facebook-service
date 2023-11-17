package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.PushSetting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PushSettingRepository extends MongoRepository<PushSetting, String> {
    Optional<PushSetting> findByUserId(String userId);
}
