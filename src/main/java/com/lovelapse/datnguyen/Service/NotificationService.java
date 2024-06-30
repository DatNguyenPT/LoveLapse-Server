package com.lovelapse.datnguyen.Service;

import com.google.firebase.database.*;
import com.google.firebase.messaging.*;
import com.lovelapse.datnguyen.DTO.NotificationModel;
import com.lovelapse.datnguyen.Repository.NotiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    @Autowired
    private NotiRepo notiRepo;

    public String sendNotificationToAllUsers(NotificationModel notificationModel) {
        DatabaseReference usersRef = firebaseDatabase.getReference("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String fcmToken = userSnapshot.child("fcmToken").getValue(String.class);
                    if (fcmToken != null) {
                        System.out.println("Sending notification to: " + fcmToken);
                        sendNotification(notificationModel, fcmToken);
                        notiRepo.save(notificationModel);
                    } else {
                        System.out.println("No FCM token found for user: " + userSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading users: " + databaseError.getCode());
            }
        });

        return "Notifications sent successfully";
    }

    private void sendNotification(NotificationModel notificationModel, String recipientToken) {
        Notification notification = Notification.builder()
                .setTitle(notificationModel.getTitle())
                .setBody(notificationModel.getBody())
                .build();

        Message message = Message.builder()
                .setToken(recipientToken)
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
            System.out.println("Notification sent to: " + recipientToken);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            System.err.println("Log error: " + e.getMessage());
            System.err.println("Failed to send notification to: " + recipientToken);
        }
    }


    public List<NotificationModel>getAllData(){
        return notiRepo.findAll();
    }

    public void clearNoti(){
        notiRepo.deleteAll();
    }
}
