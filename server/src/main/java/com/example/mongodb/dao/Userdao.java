package com.example.mongodb.dao;

import com.example.mongodb.objects.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Userdao extends MongoRepository<User, String> {
    User findByHandle(String handle);
    User findByEmail(String email);
}