package com.kiot.sample.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiot.sample.dto.ResponseDto;
import com.kiot.sample.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String ACCESS_TOKEN = "Authorization";
    public static final String REFRESH_TOKEN = "Refresh-Token";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveToken(request, "Access");
        String refreshToken = jwtTokenProvider.resolveToken(request, "Refresh");
        Authentication authentication = null;

        if (StringUtils.hasText(accessToken)) {
            // accessToken 값이 유효하다면 인증resolveToken을 조회해서 security context에 인증 정보 저장
            if (jwtTokenProvider.validateToken(accessToken)) {
                log.info("어세스 토큰이 존재합니다.");
                authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (StringUtils.hasText(refreshToken)) {
                if (jwtTokenProvider.validateToken(refreshToken)) {
                    log.info("어세스 토큰 X , 리프레쉬 토큰 O.");
                    // accessToken 값이 만료 & refreshToken 존재
                    String memberId = jwtTokenProvider.getMemberId(refreshToken);
                    String newAccessToken = jwtTokenProvider.createToken(memberId, "Access");
                    response.setHeader(JwtFilter.ACCESS_TOKEN, "Bearer " + newAccessToken);

                    authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                   log.info("두 토큰 모두 만료 됐습니다.");
                }
            }
        }
        filterChain.doFilter(request, response);

    }
}
