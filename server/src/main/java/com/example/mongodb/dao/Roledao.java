
package com.example.mongodb.dao;

import com.example.mongodb.objects.Role;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface Roledao extends MongoRepository<Role, String> {

    Role findByRole(String role);
}