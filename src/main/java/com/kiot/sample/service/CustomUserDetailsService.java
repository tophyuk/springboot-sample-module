package com.kiot.sample.service;

import com.kiot.sample.auth.CustomUserDetails;
import com.kiot.sample.domain.Member;
import com.kiot.sample.mapper.MemberMapper;
import com.kiot.sample.mapper.auth.UserDetailsMapper;
import com.kiot.sample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberIdAndDeleteYn(memberId, 'N').orElseThrow(() -> new UsernameNotFoundException(memberId + "는 존재하지 않는 아이디입니다."));
        CustomUserDetails customUserDetails = userDetailsMapper.toUserDetails(member);
        return customUserDetails;
    }

}
