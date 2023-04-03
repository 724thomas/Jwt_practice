package com.example.jwt_practice.controller;


import com.example.jwt_practice.dto.UserJoinRequest;
import com.example.jwt_practice.dto.UserLoginRequest;
import com.example.jwt_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/test")
    public ResponseEntity<String> getTest(){
        return ResponseEntity.ok().body("get Test");
    }

    @PostMapping("/test")
    public ResponseEntity<String> postTest(){
        return ResponseEntity.ok().body("post Test");
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(UserJoinRequest dto){
        userService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/userlogin")
    public ResponseEntity<String> login(UserLoginRequest dto){
        String token = userService.login(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(userService.login(dto.getUserName(), dto.getPassword()));
    }
}
