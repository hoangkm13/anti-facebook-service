package com.example.antifacebookservice.repository;

import com.example.antifacebookservice.entity.Mark;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends MongoRepository<Mark, String> {

    @Query("{'_id' : { $in : ?0 } }")
    List<Mark> findMarksByChildComments(List<String> ids, Sort sort);

    List<Mark> findByPostId(String postId);

    boolean existsByPostIdAndUserId(String postId, String userId);
}
