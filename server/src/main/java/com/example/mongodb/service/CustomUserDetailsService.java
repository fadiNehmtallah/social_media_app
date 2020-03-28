package com.example.mongodb.service;

import java.util.ArrayList;

import com.example.mongodb.dao.Userdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private Userdao userdao;

    

    public com.example.mongodb.objects.User findUserByHandle(String handle) {
        return userdao.findByHandle(handle);
    }
    public com.example.mongodb.objects.User findUserByEmail(String email) {
        return userdao.findByEmail(email);
    }
 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.mongodb.objects.User user = userdao.findByHandle(username);
        return  new User(username, user.getPassword(), new ArrayList<>());
    }

    


    
    
}