package com.example.mongodb.service;

import java.util.List;

import com.example.mongodb.dao.Screamdao;
import com.example.mongodb.objects.Screams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomScreamDetailsService {
    @Autowired
    private Screamdao screamdao;

    public List<Screams> findScreamByUserHandle(String handle) {
        return  screamdao.findByUserHandle(handle);
    }
    public Screams findScreamByScreamId(long screamId){
        return screamdao.findByScreamId(screamId);
    }
    public void deleteScreamByScreamId(long screamId){
        screamdao.deleteByScreamId(screamId);
    }

}