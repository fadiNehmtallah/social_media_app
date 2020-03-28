package com.example.mongodb.objects;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notifications")
public class Notifications{
    private Long screamId;
    private LocalDateTime createdAt;
    private String recipient;
    private String sender;
    private String type;
    private Boolean read;
    @Id
    private ObjectId notificationsId;


    public Notifications(Long screamId, String recipient, String sender, String type) {
        this.screamId = screamId;
        this.createdAt = LocalDateTime.now();
        this.recipient = recipient;
        this.sender = sender;
        this.type = type;
        this.read = false;
    }

    public ObjectId getNotificationId() {
        return this.notificationsId;
    }

    public void setNotificationsId(ObjectId notificationsId) {
        this.notificationsId = notificationsId;
    }


    public long getScreamId() {
        return this.screamId;
    }

    public void setScreamId(long screamId) {
        this.screamId = screamId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isRead() {
        return this.read;
    }

    public Boolean getRead() {
        return this.read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    





}