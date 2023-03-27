package com.example.jwt_practice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {

    @PostMapping("/review")
    public ResponseEntity<String> review(Authentication authentication){
        return ResponseEntity.ok().body(authentication.getName() + "님의 리뷰 등록 성공");

    }
}
