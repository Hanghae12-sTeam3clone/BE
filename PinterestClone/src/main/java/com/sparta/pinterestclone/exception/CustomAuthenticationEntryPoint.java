package com.sparta.pinterestclone.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception.equals(Exception.NOT_FOUND_TOKEN)) {
            exceptionHandler(response, Exception.NOT_FOUND_TOKEN);
            return;
        }

        if (exception.equals(Exception.INVALID_TOKEN)) {
            exceptionHandler(response, Exception.INVALID_TOKEN);
            return;
        }

        if (exception.equals(Exception.NOT_FOUND_USER)) {
            exceptionHandler(response, Exception.NOT_FOUND_USER);
        }
    }

    public void exceptionHandler(HttpServletResponse response, Exception exception) {
        response.setStatus(exception.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.toResponseEntity(exception));
            response.getWriter().write(json);
            log.error(exception.getMsg());
        } catch (java.lang.Exception e) {
            log.error(e.getMessage());
        }
    }

}
