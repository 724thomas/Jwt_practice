package com.example.jwt_practice.service;

import com.example.jwt_practice.entity.User;
import com.example.jwt_practice.exception.AppException;
import com.example.jwt_practice.exception.ErrorCode;
import com.example.jwt_practice.repository.UserRepository;
import com.example.jwt_practice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;


    @Value("${jwt.token.secret}")
    private String key;
    private Long expiredMs = 1000 * 60 * 60 * 24 * 7L ; //일주일

    public String join(String userName, String password){

        //username 중복 체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + " 이미 존재하는 회원입니다.");
                });

        //저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);

        return "success";
    }

    public String login(String userName, String password){
        //userName 없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + " 존재하지 않는 회원입니다."));

        //password 틀림
        if(!encoder.matches(password, selectedUser.getPassword())){ //순서 중요. inputpassword, DBpassword
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }

        String token = JwtUtil.createToken(selectedUser.getUserName(), key, expiredMs);
        return token;
    }
}
