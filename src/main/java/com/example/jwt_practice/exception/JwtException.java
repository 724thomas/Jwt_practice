package com.example.jwt_practice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;
}
