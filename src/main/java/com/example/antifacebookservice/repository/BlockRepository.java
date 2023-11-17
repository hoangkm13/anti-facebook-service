package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.BlockUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends MongoRepository<BlockUser, String> {
    List<BlockUser> findAllByBlockerId(String userId);

    boolean existsByBlockerIdAndBlockedId(String blockerId, String blockedId);

    Optional<BlockUser> findByBlockerIdAndBlockedId(String blockerId, String blockedId);

}
