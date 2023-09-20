package com.kiot.sample.handler.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiot.sample.dto.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        // 인증문제가 발생했을 때 이 부분을 호출한다.
        response.setStatus(SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("F021")
                .message("Access Token, Refresh Token 둘 다 만료되었습니다.")
                .data(null)
                .build();

        String json = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(json);
        //response.sendRedirect("/auth/login");


    }
}
