package com.example.mongodb.service;

import java.util.List;

import com.example.mongodb.dao.Commentdao;
import com.example.mongodb.objects.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomCommentDetailsService{
    @Autowired
    private Commentdao commentdao;
    public List<Comment> findCommentByScreamId(long screamId){
        return commentdao.findByScreamId(screamId);
    }
    public List<Comment> findCommentByUserHandle(String userHandle){
        return commentdao.findByUserHandle(userHandle);
    }
}