package com.example.jwt_practice.service;

import com.example.jwt_practice.exception.AppException;
import com.example.jwt_practice.exception.ErrorCode;
import com.example.jwt_practice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.token.secret}")
    private String key;
//    private Long expiredMs = 1000 * 60 * 60 * 24 * 7L ; //일주일
    private Long expiredMs = 1000 * 60 * 30L ; // 30분

    public String join(String userName, String password){


        //username 중복 체크
        if(userName.equals("유저이름이 이미 존재할때")){
            throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + ": 이미 존재하는 회원입니다.");
        }

        System.out.println("유저 저장");
        return "success";
    }

    public String login(String userName, String password){
        //userName 없음
        if(userName.equals("유저가 존재하지 않을때")){
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + ": 존재하지 않는 회원입니다.");
        }

        //password 틀림
        if (password.equals("비밀번호가 틀렸을때")){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }

        String token = JwtUtil.createToken(userName, key, expiredMs);
        return token;
    }
}
