package com.lovelapse.datnguyen.Service;

import com.lovelapse.datnguyen.DTO.Connections;
import com.lovelapse.datnguyen.Repository.ConnectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SendConnectionService {
    @Autowired
    ConnectionRepo connectionRepo;

    public SendConnectionService(ConnectionRepo connectionRepo){
        this.connectionRepo = connectionRepo;
    }

    public void addNewConnections(String from, String to){
        Connections connections = new Connections();
        connections.setFromUser(from);
        connections.setToUser(to);
        connections.setReplied(false);
        connections.setDaySendOrReceive(LocalDate.now().toString());
        connectionRepo.save(connections);

    }
    // Current user's message is replied
    public void beReplied(String from, String to, String currentUser){
        Connections connections = connectionRepo.findAll().stream()
                .filter(con -> con.getFromUser().equals(currentUser)) // User sent
                .findFirst()
                .orElse(null);
        if(connections != null)
            connections.setReplied(true);
    }

    // User replied to other's message
    public void reply(String from, String to, String currentUser){
        Connections connections = connectionRepo.findAll().stream()
                .filter(con -> con.getToUser().equals(currentUser)) // User receive
                .findFirst()
                .orElse(null);
        if(connections != null)
            connections.setReplied(true);
    }

    // Find who the user sent connections to
    public List<Connections> findSendConnections(String currentUser){
        List<Connections>list = connectionRepo.findAll().stream()
                .filter(con -> con.getFromUser().equals(currentUser))
                .toList();
        return list;
    }

    // Find who the user receive connections from
    public List<Connections> findReceiveConnections(String currentUser){
        List<Connections>list = connectionRepo.findAll().stream()
                .filter(con -> con.getToUser().equals(currentUser))
                .toList();
        return list;
    }
}
