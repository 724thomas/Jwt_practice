package com.example.jwt_practice.configuration;

import com.example.jwt_practice.exception.AppException;
import com.example.jwt_practice.exception.ErrorCode;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
            IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AppException e) {
            ErrorCode errorCode = e.getErrorCode();
            System.out.println("error response : " + errorCode.name());
            setErrorResponse(response, errorCode);
        }
    }

    public void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{ \"status\" : " + errorCode.getHttpStatus().value()
                + ", \"error\" : \"" + errorCode.getHttpStatus().name()
                + "\", \"code\" : \"" + errorCode.name()
                + "\", \"message\" : \"" + errorCode.getMessage()
                + "\" }");
    }
}
