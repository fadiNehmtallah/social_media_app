package com.example.mongodb.dao;

import java.util.List;

import com.example.mongodb.objects.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Commentdao extends MongoRepository<Comment, String>{
    List<Comment> findByScreamId(long screamId);
    List<Comment> findByUserHandle(String userHandle);
}