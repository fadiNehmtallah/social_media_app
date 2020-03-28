package com.example.mongodb.dao;

import java.util.List;

import com.example.mongodb.objects.Notifications;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Notificationdao extends MongoRepository<Notifications, String> {
    List<Notifications> findByRecipient(String recipient);
    List<Notifications> findByScreamId(long screamId);
    Notifications findByNotificationsId(ObjectId notificationsIds);
	Notifications findByScreamIdAndSenderAndType(long screamId,String sender,String type);


}