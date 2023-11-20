package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Optional<Notification> findByNotificationId(String notificationId);

    List<Notification> findAllByUserId(String userId);

    List<Notification> findAllByUserIdAndIsRead(String userId, Boolean isRead);
}
