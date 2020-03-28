package com.example.mongodb.service;

import java.util.List;

import com.example.mongodb.dao.Likedao;
import com.example.mongodb.objects.Like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomLikeDetailsService {
    @Autowired
    private Likedao likedao;

    public List <Like> findLikeByUserHandle(String userHandle) {
        return likedao.findByUserHandle(userHandle);
    }
    public Like findLikeByScreamIdAndUserHandle(String userHandle,long screamId){
        return likedao.findByScreamIdAndUserHandle(screamId,userHandle);
    }
    public List<Like> findLikeByScreamId(long screamId){
        return likedao.findByScreamId(screamId);
    }

}