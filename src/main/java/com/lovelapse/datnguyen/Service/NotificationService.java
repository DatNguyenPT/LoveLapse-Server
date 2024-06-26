package com.lovelapse.datnguyen.Service;

import com.google.firebase.database.*;
import com.google.firebase.messaging.*;
import com.lovelapse.datnguyen.DTO.NotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    public String sendNotificationToAllUsers(NotificationModel notificationModel) {
        DatabaseReference usersRef = firebaseDatabase.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userKey = userSnapshot.getKey();
                    // Assuming the user node key is the FCM token or something similar
                    String recipientToken = userKey;

                    sendNotification(notificationModel, recipientToken);
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
            System.err.println("Failed to send notification to: " + recipientToken);
        }
    }
}
