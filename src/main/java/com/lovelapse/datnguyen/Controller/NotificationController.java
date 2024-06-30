package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.NotificationModel;
import com.lovelapse.datnguyen.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("/send")
    public String sendNoti(@RequestBody NotificationModel notificationModel){
        return notificationService.sendNotificationToAllUsers(notificationModel);
    }

    @PostMapping("/delete")
    public void clearAllNoti(){
        notificationService.clearNoti();
    }

    @GetMapping("/getAllNoti")
    public List<NotificationModel> getAllNotis(){
        return notificationService.getAllData();
    }
}
