package com.kiot.sample.mapper;

import com.kiot.sample.auth.CustomUserDetails;
import com.kiot.sample.domain.Board;
import com.kiot.sample.domain.Member;
import com.kiot.sample.dto.board.BoardDto;
import com.kiot.sample.dto.member.MemberDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MemberMapper
{

    public MemberDto toMemberDto(Member member) {
        return new MemberDto(
                member.getMemberId(),
                member.getName(),
                member.getPassword(),
                member.getEmail(),
                member.getTelNum(),
                member.getImageUrl(),
                member.getLoginType(),
                member.getBirthDate(),
                member.getRole(),
                member.getDeleteYn()
        );
    }

    public Member toMemberEntity(MemberDto memberDto) {
        return Member.builder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .telNum(memberDto.getTelNum())
                .loginType(memberDto.getLoginType())
                .role(memberDto.getRole())
                .deleteYn(memberDto.getDeleteYn())
                .build();
    }

}
