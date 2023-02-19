package com.example.jwt_practice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {
    public static String createToken(String userName, String key, long expireTimemMs) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimemMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
