package com.example.mongodb.objects;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("comments")
public class Comment{
    private LocalDateTime createdAt;
    private long screamId;
    private String userHandle;
    private String userImage;
    private String body;
    @Id
    private ObjectId commentId;

    

    public Comment(long screamId, String userHandle, String userImage, String body) {
        this.createdAt = LocalDateTime.now();
        this.screamId = screamId;
        this.userHandle = userHandle;
        this.userImage = userImage;
        this.body = body;
        
    }


    
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getScreamId() {
        return this.screamId;
    }

    public void setScreamId(long screamId) {
        this.screamId = screamId;
    }

    public String getUserHandle() {
        return this.userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ObjectId getCommentId() {
        return this.commentId;
    }

    public void setCommentId(ObjectId commentId) {
        this.commentId = commentId;
    }





}
