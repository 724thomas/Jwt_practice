package com.example.jwt_practice.configuration;

import com.example.jwt_practice.exception.AppException;
import com.example.jwt_practice.exception.ErrorCode;
import com.example.jwt_practice.service.UserService;
import com.example.jwt_practice.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter  extends OncePerRequestFilter {

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal");

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : {}", authorization);


        if(authorization == null || !authorization.startsWith("Bearer")){
            log.error("authorization is null or not Bearer");

            HashMap<String, String> temp = new HashMap<>();
            temp.put("test", "value");

            ResponseEntity<HashMap<String, String>> responseEntity = ResponseEntity.badRequest().body(temp);
            response.setStatus(responseEntity.getStatusCodeValue());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // set the content type to application/json
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer");
            throw new AppException(ErrorCode.USERNAME_DUPLICATED, "test");

//            return;
//
        }

        if (authorization.split(" ").length != 2) {
            log.error("Token is not valid");
            filterChain.doFilter(request, response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1].trim();

        // Token Expired 여부 확인
        boolean isExpired = true;
//        if (JwtUtil.isExpired(token, secretKey)) {
        if (isExpired) {
            log.error("Token is Expired");
            filterChain.doFilter(request, response);
            return;
        }


        // UserName Token에서 꺼내기
        String userName = JwtUtil.getUserName(token, secretKey);
        log.info("userName : {}", userName);

        //권한 부여
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken("A", null, List.of(new SimpleGrantedAuthority("USER")));

        //Detail을 넣어줍니다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
