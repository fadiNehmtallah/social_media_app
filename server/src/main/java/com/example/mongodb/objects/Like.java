package com.example.mongodb.objects;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("likes")
public class Like{
    private String userHandle;
    private long screamId;
    @Id
    private ObjectId likeId;


    public Like(String userHandle, long screamId) {
        this.userHandle = userHandle;
        this.screamId = screamId;
    }


    public String getUserHandle() {
        return this.userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public long getScreamId() {
        return this.screamId;
    }

    public void setScreamId(long screamId) {
        this.screamId = screamId;
    }

    public ObjectId getLikeId() {
        return this.likeId;
    }

    public void setLikeId(ObjectId likeId) {
        this.likeId = likeId;
    }
}
