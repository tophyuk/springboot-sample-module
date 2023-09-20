package com.kiot.sample.jwt;

import com.kiot.sample.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private String secretKey = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";
    private long accessTokenValidTime = 1000L * 60 * 1; // 30분
    /*private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7일*/

    private long refreshTokenValidTime = 1000L * 60 * 4;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 유저 정보(memberId)를 가지고 AccessToken, RefreshToken을 생성하는 메소드
    public String createToken(String memberId, String type) {
        Claims claims = Jwts.claims().setSubject(memberId); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        long time = type.equals("Access") ? accessTokenValidTime : refreshTokenValidTime;

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(new Date(now.getTime() + time)) // 토큰 유효 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, key 값
                .compact();
    }

    // 토큰 정보 추출
    String resolveToken(HttpServletRequest request, String tokenType) {
        String token = "";
        if (tokenType.equals("Access")) {
            token = request.getHeader(JwtFilter.ACCESS_TOKEN);
        } else if (tokenType.equals("Refresh")) {
            token = request.getHeader(JwtFilter.REFRESH_TOKEN);
        }

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }


    // 토큰 유효성, 만료일자 확인
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
            log.info("지원하지 않는 JWT 토큰입니다.");
        }catch(IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getMemberId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 획득
    public String getMemberId(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch(ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return e.getClaims().getSubject();
        }
    }


}
