package com.example.locationtrackingbackend.controllers;

import com.example.locationtrackingbackend.models.User;
import com.example.locationtrackingbackend.repositories.UserRepository;
import com.example.locationtrackingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users=userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user-profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        User user=userService.findUserProfileByJwt(jwt);
        return ResponseEntity.ok(user);
    }
}
