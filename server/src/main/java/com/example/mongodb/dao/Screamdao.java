package com.example.mongodb.dao;

import java.util.List;

import com.example.mongodb.objects.Screams;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Screamdao extends MongoRepository<Screams, String> {
    List<Screams> findByUserHandle(String handle);
    Screams findByScreamId(long screamId);
    void deleteByScreamId(long screamId);
}