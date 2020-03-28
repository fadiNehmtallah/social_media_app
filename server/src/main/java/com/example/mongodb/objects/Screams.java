package com.example.mongodb.objects;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "screams")
public class Screams{
    @Id
    private long screamId;
    private String body;
    private LocalDateTime createdAt;
    private int commentCount;
    private int likeCount;
    private String userHandle;
    private String userImage;

    public static final String SEQUENCE_NAME = "screams_sequence";
    

    public Screams(String body, String userHandle, String userImage) {
        this.body = body;
        this.createdAt = LocalDateTime.now();
        this.userHandle = userHandle;
        this.userImage = userImage;
        this.commentCount = 0;
        this.likeCount = 0;
    }

    public long getScreamId() {
        return this.screamId;
    }

    public void setScreamId(long screamId) {
        this.screamId = screamId;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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


}