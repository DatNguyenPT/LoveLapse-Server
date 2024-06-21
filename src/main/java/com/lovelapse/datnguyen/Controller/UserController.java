package com.lovelapse.datnguyen.Controller;

import com.lovelapse.datnguyen.DTO.UserModel;
import com.lovelapse.datnguyen.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/adduser")
    public ResponseEntity<?> addNewUser(UserModel userModel){
        userService.addUser(userModel);
        return ResponseEntity.ok("Add new user successfully");
    }
}
