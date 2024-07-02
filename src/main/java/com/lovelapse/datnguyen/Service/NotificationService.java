package com.lovelapse.datnguyen.Service;

import com.google.firebase.database.*;
import com.google.firebase.messaging.*;
import com.lovelapse.datnguyen.DTO.NotificationModel;
import com.lovelapse.datnguyen.Repository.NotiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        String[] result = {""};
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String fcmToken = userSnapshot.child("fcmToken").getValue(String.class);
                    if (fcmToken != null) {
                        System.out.println("Sending notification to: " + fcmToken);
                        sendNotification(notificationModel, fcmToken);
                        notiRepo.save(notificationModel);
                        result[0] = "Send Notification Successfully";
                    } else {
                        System.out.println("No FCM token found for user: " + userSnapshot.getKey());
                        result[0] = "Send Notification Failed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading users: " + databaseError.getCode());
                result[0] = "Send Notification Failed";
            }
        });

        return result[0];
    }

    public String sendNotification(NotificationModel notificationModel, String recipientToken) {
        String result = "";
        Message message = Message.builder()
                .setToken(recipientToken)
                .setNotification(Notification.builder()
                        .setTitle(notificationModel.getTitle())
                        .setBody(notificationModel.getBody())
                        .build())
                .putData("title", notificationModel.getTitle())
                .putData("body", notificationModel.getBody())
                .build();

        try {
            firebaseMessaging.send(message);
            System.out.println("Notification sent to: " + recipientToken);
            result = "Sent Notification Successfully";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            System.err.println("Log error: " + e.getMessage());
            System.err.println("Failed to send notification to: " + recipientToken);
            result = e.getMessage();
        }
        return result;
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatterEnglish1 = DateTimeFormatter.ofPattern("MMMM dd,yyyy - hh:mm a", Locale.ENGLISH);
        DateTimeFormatter formatterEnglish2 = DateTimeFormatter.ofPattern("MMMM d,yyyy - hh:mm a", Locale.ENGLISH);
        DateTimeFormatter formatterVietnamese1 = DateTimeFormatter.ofPattern("MMMM dd,yyyy - hh:mm a", new Locale("vi"));
        DateTimeFormatter formatterVietnamese2 = DateTimeFormatter.ofPattern("MMMM d,yyyy - hh:mm a", new Locale("vi"));

        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(dateTimeString, formatterEnglish1);
        } catch (DateTimeParseException e1) {
            try {
                dateTime = LocalDateTime.parse(dateTimeString, formatterEnglish2);
            } catch (DateTimeParseException e2) {
                try {
                    dateTime = LocalDateTime.parse(dateTimeString, formatterVietnamese1);
                } catch (DateTimeParseException e3) {
                    dateTime = LocalDateTime.parse(dateTimeString, formatterVietnamese2);
                }
            }
        }
        return dateTime;
    }

    public static long calculateSecondsUntil(String targetTimeString) {
        LocalDateTime targetTime = parseDateTime(targetTimeString);
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        Duration duration = Duration.between(now, targetTime);
        return duration.getSeconds();
    }

    @Scheduled(fixedRate = 2000)
    public void sendNewMessageNotification() {
        System.out.println("New message");
        Map<String, String> userMap = new HashMap<>();
        DatabaseReference usersRef = firebaseDatabase.getReference("chat");
        System.out.println(usersRef.toString());

        DatabaseReference userList = firebaseDatabase.getReference("Users");
        System.out.println(userList.toString());

        userList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String fcmToken = userSnapshot.child("fcmToken").getValue(String.class);
                    if (fcmToken != null) {
                        userMap.put(userSnapshot.getKey(), fcmToken);
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

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.hasChild("receiverNumber")) {
                        String to = userSnapshot.child("receiverNumber").getValue(String.class);

                        if (to != null && !to.isEmpty()) {
                            DatabaseReference receiveMessageTime = userSnapshot.getRef().child("dateTime");
                            receiveMessageTime.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot receiveMessageTime) {
                                    String receiveDate = receiveMessageTime.getValue(String.class);
                                    System.out.println("to: " + to);
                                    System.out.println("date: " + receiveDate);
                                    System.out.println("seconds: " + calculateSecondsUntil(receiveDate));
                                    if (calculateSecondsUntil(receiveDate) < 10 && userMap.containsKey(to)) {
                                        Message message = Message.builder()
                                                .setToken(userMap.get(to))
                                                .setNotification(Notification.builder()
                                                        .setTitle("Tin nhắn mới")
                                                        .setBody("Một người đã gửi tin nhắn cho bạn")
                                                        .build())
                                                .putData("title", "Tin nhắn mới")
                                                .putData("body", "Một người đã gửi tin nhắn cho bạn")
                                                .build();

                                        try {
                                            firebaseMessaging.send(message);
                                            System.out.println("Notification sent to: " + to);
                                        } catch (FirebaseMessagingException e) {
                                            e.printStackTrace();
                                            System.err.println("Log error: " + e.getMessage());
                                            System.err.println("Failed to send notification to: " + to);
                                        }
                                    }
                                    System.out.println("New message for user: " + userSnapshot.getKey());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("Error: " + databaseError.getCode());
                                }
                            });
                        } else {
                            System.out.println("No message found for: " + userSnapshot.getKey());
                        }
                    } else {
                        System.out.println("No message found for: " + userSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading users: " + databaseError.getCode());
            }
        });
    }

    @Scheduled(fixedRate = 86400000)
    public void scheduleLoveDayUpdate() {
        DatabaseReference usersRef = firebaseDatabase.getReference("Partner");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.hasChild("partner")) {
                        String partner = userSnapshot.child("partner").getValue(String.class);
                        if (partner != null && !partner.isEmpty()) {
                            DatabaseReference dayCountRef = userSnapshot.getRef().child("daycount");
                            dayCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dayCountSnapshot) {
                                    Long dayCount = dayCountSnapshot.getValue(Long.class);
                                    if (dayCount == null) {
                                        dayCount = 0L;
                                    }
                                    dayCountRef.setValueAsync(dayCount + 1);
                                    System.out.println("Daycount updated for user: " + userSnapshot.getKey());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("Error updating daycount: " + databaseError.getCode());
                                }
                            });
                        } else {
                            System.out.println("No lover found for user: " + userSnapshot.getKey());
                        }
                    } else {
                        System.out.println("No lover field found for user: " + userSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading users: " + databaseError.getCode());
            }
        });
    }

    public void updateFCMTokens(String token) {
        DatabaseReference usersRef = firebaseDatabase.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String currentToken = userSnapshot.child("fcmToken").getValue(String.class);
                    String newToken = token;
                    if (newToken != null && !newToken.isEmpty() && !newToken.equals(currentToken)) {
                        userSnapshot.getRef().child("fcmToken").setValue(newToken, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                System.out.println("Token update failed, " + databaseError.getMessage());
                            } else {
                                System.out.println("Token updated successfully for user: " + userSnapshot.getKey());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading users: " + databaseError.getCode());
            }
        });
    }


    public List<NotificationModel> getAllData() {
        return notiRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void clearNoti() {
        notiRepo.deleteAll();
    }
}
