package com.kiot.sample.controller;

import com.kiot.sample.dto.member.MemberDto;
import com.kiot.sample.dto.ResponseDto;
import com.kiot.sample.jwt.JwtFilter;
import com.kiot.sample.jwt.TokenInfo;
import com.kiot.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
/*    @GetMapping("/login")
    public ResponseEntity<ResponseDto> login() {

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S001")
                .message("로그인 페이지 조회에 성공하였습니다.")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }*/

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> loginAction(@RequestBody MemberDto memberDto) {

        TokenInfo token = memberService.login(memberDto);
        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.ACCESS_TOKEN, "Bearer " + token.getAccessToken());
        httpHeaders.add(JwtFilter.REFRESH_TOKEN, "Bearer " + token.getRefreshToken());

        return new ResponseEntity<>(session, httpHeaders, HttpStatus.OK);

    }


}
