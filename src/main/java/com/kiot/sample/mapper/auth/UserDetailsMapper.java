package com.kiot.sample.mapper.auth;

import com.kiot.sample.auth.CustomUserDetails;
import com.kiot.sample.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsMapper {

    public Member toMemberEntity(CustomUserDetails customUserDetails) {
        return Member.builder()
                .memberId(customUserDetails.getMemberId())
                .name(customUserDetails.getName())
                .password(customUserDetails.getPassword())
                .email(customUserDetails.getEmail())
                .telNum(customUserDetails.getTelNum())
                .loginType(customUserDetails.getLoginType())
                .role(customUserDetails.getRole())
                .deleteYn(customUserDetails.getDeleteYn())
                .build();
    }

    public CustomUserDetails toUserDetails(Member member) {
        return new CustomUserDetails(
                member.getMemberId(),
                member.getName(),
                member.getPassword(),
                member.getEmail(),
                member.getLoginType(),
                member.getRole(),
                member.getDeleteYn()
        );
    }
}
