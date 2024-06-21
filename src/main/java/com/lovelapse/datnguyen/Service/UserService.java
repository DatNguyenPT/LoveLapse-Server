package com.lovelapse.datnguyen.Service;

import com.lovelapse.datnguyen.DTO.UserModel;
import com.lovelapse.datnguyen.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public void addUser(UserModel userModel){
        userRepo.save(userModel);
    }

    public void deleteUser(UserModel userModel){
        userRepo.delete(userModel);
    }
}
