package com.example.mongodb.dao;

import java.util.List;

import com.example.mongodb.objects.Like;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Likedao extends MongoRepository<Like, String> {
    List<Like> findByUserHandle(String userHandle);
    List<Like> findByScreamId(long screamId);
    Like findByScreamIdAndUserHandle(long screamId,String userHandle);


}