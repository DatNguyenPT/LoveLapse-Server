package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.Connections;
import com.lovelapse.datnguyen.Service.SendConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connection")
public class ConnectionController {
    @Autowired
    private SendConnectionService sendConnectionService;

    public ConnectionController(SendConnectionService sendConnectionService){
        this.sendConnectionService = sendConnectionService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> addConnection(@RequestBody Connections connections){
        sendConnectionService.addNewConnections(connections);
        return ResponseEntity.ok("New connection has been added");
    }

    @GetMapping("/getsent")
    public ResponseEntity<?> getAllSent(@RequestParam String currentUser){
        return ResponseEntity.ok(sendConnectionService.findSendConnections(currentUser));
    }

    @GetMapping("/getreceive")
    public ResponseEntity<?> getAllReceive(@RequestParam String currentUser){
        return ResponseEntity.ok(sendConnectionService.findReceiveConnections(currentUser));
    }

    @PostMapping("/getreplied")
    public void getReplied(@RequestParam String from, @RequestParam String to, @RequestParam String currentUser){
        sendConnectionService.beReplied(from, to, currentUser);
    }

    @PostMapping("/reply")
    public void reply(@RequestParam String from, @RequestParam String to, @RequestParam String currentUser){
        sendConnectionService.reply(from, to, currentUser);
    }

    // ADMIN
    @GetMapping("/size")
    public ResponseEntity<?> getSize(){
        return new ResponseEntity<>(sendConnectionService.getSize(), HttpStatus.OK);
    }
}

