package com.kiot.sample.handler.auth;

import com.kiot.sample.auth.CustomOAuth2User;
import com.kiot.sample.auth.CustomUserDetails;
import com.kiot.sample.domain.Member;
import com.kiot.sample.jwt.JwtTokenProvider;
import com.kiot.sample.mapper.auth.UserDetailsMapper;
import com.kiot.sample.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserDetailsMapper userDetailsMapper;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Principal에서 꺼낸 customOAuth2User = {}", customUserDetails);

        // todo - 최초 로그인이면 회원가입 시킨다.
        Member member = userDetailsMapper.toMemberEntity(customUserDetails);
        memberRepository.findByMemberIdAndDeleteYnAndLoginType(member.getMemberId(), 'N', member.getLoginType())
                .orElseGet(() -> memberRepository.save(member));

        // 액세스 토큰 가져오기
        // todo - 엑세스토큰 만료 시에 리프레시 토큰 통해 엑세스토큰 갱신하기
        if (authentication instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            String tokenValue = accessToken.getTokenValue();

            // 액세스 토큰이 만료되었을 경우 refreshToken 사용

            // 클라이언트에게 JWT를 전달
            response.addHeader("Authorization", tokenValue);
        }

        //getRedirectStrategy().sendRedirect(request,response, "/main");


    }
}
