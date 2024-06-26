package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.NotificationModel;
import com.lovelapse.datnguyen.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("/send")
    public String sendNoti(@RequestBody NotificationModel notificationModel){
        return notificationService.sendNotificationToAllUsers(notificationModel);
    }
}
