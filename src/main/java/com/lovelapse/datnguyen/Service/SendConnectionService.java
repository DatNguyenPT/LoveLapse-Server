package com.lovelapse.datnguyen.Service;

import com.lovelapse.datnguyen.DTO.Connections;
import com.lovelapse.datnguyen.Repository.ConnectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendConnectionService {
    @Autowired
    ConnectionRepo connectionRepo;

    public SendConnectionService(ConnectionRepo connectionRepo){
        this.connectionRepo = connectionRepo;
    }

    public void addNewConnections(Connections connections){
        connectionRepo.save(connections);
    }

    public void beReplied(String from, String to, String currentUser){
        Connections connections = connectionRepo.findAll().stream()
                .filter(con -> con.getFromUser().equals(currentUser) && con.getFromUser().equals(from) && con.getToUser().equals(to))
                .findFirst()
                .orElse(null);
        if(connections != null) {
            connections.setReplied(true);
            connectionRepo.save(connections); // Save lại sau khi thay đổi
        }
    }

    public void reply(String from, String to, String currentUser){
        Connections connections = connectionRepo.findAll().stream()
                .filter(con -> con.getToUser().equals(currentUser) && con.getFromUser().equals(from) && con.getToUser().equals(to))
                .findFirst()
                .orElse(null);
        if(connections != null) {
            connections.setReplied(true);
            connectionRepo.save(connections); // Save lại sau khi thay đổi
        }
    }

    public List<Connections> findSendConnections(String currentUser){
        return connectionRepo.findAll().stream()
                .filter(con -> con.getFromUser().equals(currentUser))
                .collect(Collectors.toList());
    }

    public List<Connections> findReceiveConnections(String currentUser){
        return connectionRepo.findAll().stream()
                .filter(con -> con.getToUser().equals(currentUser))
                .collect(Collectors.toList());
    }

    public int getSize(){
        return (int) connectionRepo.count();
    }
}
