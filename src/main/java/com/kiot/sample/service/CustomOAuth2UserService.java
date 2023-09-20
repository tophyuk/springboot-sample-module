package com.kiot.sample.service;

import com.kiot.sample.auth.*;
import com.kiot.sample.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    public final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return this.process(userRequest, oAuth2User);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google or naver
        String tokenValue = userRequest.getAccessToken().getTokenValue();

        if(provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }

        String email = oAuth2UserInfo.getEmail();
        String memberId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String password = bCryptPasswordEncoder.encode(memberId);  // 사용자가 입력한 적은 없지만 만들어준다
        Role role = Role.USER;
        String picture = oAuth2UserInfo.getPicture();
        String loginType = oAuth2UserInfo.getProvider();

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("email", email);
        attributes.put("memberId", memberId);
        attributes.put("name", name);
        attributes.put("password", password);
        attributes.put("role", role);
        attributes.put("loginType", loginType);
        attributes.put("tokenValue", tokenValue);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2UserInfo.getAttributes(), name, password, role, tokenValue, provider );
        //CustomUserDetails customUserDetails = new CustomUserDetails(memberId, name, email, password, loginType, role, 'N');
        //oAuth2User =  new DefaultOAuth2User(Collections.singleton(new OAuth2UserAuthority(attributes)), attributes, "name");
        //return oAuth2User;

        return customOAuth2User;

    }

}
