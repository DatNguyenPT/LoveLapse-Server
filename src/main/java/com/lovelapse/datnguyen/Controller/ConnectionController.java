package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.Service.SendConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {
    @Autowired
    private SendConnectionService sendConnectionService;

    public ConnectionController(SendConnectionService sendConnectionService){
        this.sendConnectionService = sendConnectionService;
    }

    @PostMapping("/connection/send")
    public ResponseEntity<?>addConnection(@RequestParam String from, @RequestParam String to){
        sendConnectionService.addNewConnections(from, to);
        return ResponseEntity.ok("New connection has been added");
    }

    @GetMapping("/connection/getsent")
    public ResponseEntity<?>getAllSent(@RequestParam String currentUser){
        return ResponseEntity.ok(sendConnectionService.findSendConnections(currentUser));
    }

    @GetMapping("/connection/getreceive")
    public ResponseEntity<?>getAllReceive(@RequestParam String currentUser){
        return ResponseEntity.ok(sendConnectionService.findReceiveConnections(currentUser));
    }

    @PostMapping("/connection/getreplied")
    public void getReplied(@RequestParam String from, @RequestParam String to, @RequestParam String currentUser){
        sendConnectionService.beReplied(from, to, currentUser);
    }

    @PostMapping("/connection/reply")
    public void reply(@RequestParam String from, @RequestParam String to, @RequestParam String currentUser){
        sendConnectionService.reply(from, to, currentUser);
    }
}
