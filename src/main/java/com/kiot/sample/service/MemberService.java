package com.kiot.sample.service;

import com.kiot.sample.domain.Member;
import com.kiot.sample.domain.Role;
import com.kiot.sample.dto.member.MemberDto;
import com.kiot.sample.dto.member.MemberUpdateDto;
import com.kiot.sample.dto.member.PasswordDto;
import com.kiot.sample.jwt.JwtTokenProvider;
import com.kiot.sample.jwt.TokenInfo;
import com.kiot.sample.mapper.MemberMapper;
import com.kiot.sample.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberMapper memberMapper;

    public boolean checkMemberId(String memberId) {
        boolean existsByEmailAndLoginType = memberRepository.existsByMemberIdAndDeleteYn(memberId, 'N');
        return existsByEmailAndLoginType;
    }

    @Transactional
    public void signup(MemberDto memberDto) {
        memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
        memberDto.setRole(Role.USER);
        memberDto.setLoginType("basic");
        memberDto.setDeleteYn('N');

        Member member = memberMapper.toMemberEntity(memberDto);
        memberRepository.save(member);
    }

    public MemberDto selectMember(Long id) {
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));
        MemberDto memberDto = memberMapper.toMemberDto(member);
        return memberDto;
    }

    public List<Member> selectMemberAll() {
        List<Member> all = memberRepository.findAll();
        return all;
    }

    @Transactional
    public void update(Long id, @NotNull MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));

        member.updateMember(memberUpdateDto.getName(), memberUpdateDto.getEmail(), memberUpdateDto.getTelNum());
    }

    @Transactional
    public void updatePassword(Long id, @NotNull PasswordDto passwordDto) {
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));

        if (bCryptPasswordEncoder.matches(passwordDto.getOldPassword(), member.getPassword())) {
            String encodePassword = bCryptPasswordEncoder.encode(passwordDto.getNewPassword());
            member.setPassword(encodePassword);
        } else {
            throw new IllegalArgumentException("현재 비밀번호가 맞지 않습니다.");
        }
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));
        member.setDeleteYn('Y');
    }

    public TokenInfo login(@NotNull MemberDto memberDto) {

        // DB에서 받아온 유저가 있는지, 있다면 패스워드가 맞는지 점검
        Member member = memberRepository.findByMemberIdAndDeleteYn(memberDto.getMemberId(), 'N').orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다"));
        if (!bCryptPasswordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(member.getMemberId(), "Access");
        String refreshToken = jwtTokenProvider.createToken(member.getMemberId(), "Refresh");


        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
