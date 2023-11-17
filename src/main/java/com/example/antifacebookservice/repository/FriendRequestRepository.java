package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    Integer countFriendRequestByUserSentId(String userSentId);

    FriendRequest findByUserSentIdAndUserReceiveId(String userSentId, String userReceiveId);
}