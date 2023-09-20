package com.kiot.sample.service;

import com.kiot.sample.domain.Member;
import com.kiot.sample.domain.Role;
import com.kiot.sample.dto.member.MemberDto;
import com.kiot.sample.jwt.JwtTokenProvider;
import com.kiot.sample.mapper.MemberMapper;
import com.kiot.sample.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    /**
     * 사용자정보
     **/
    String memberId = "jhj221";
    String name = "김민수";
    String password = "test!@34";
    String email = "gildong@google.com";
    String telNum = "010-5523-5512";
    Role role = Role.USER;

    @Test
    @DisplayName("로그인 jwt 토큰 생성")
    void loginAction() {
        memberId = "shj8278";
        password = "test!@34";
        Member member = memberRepository.findByMemberIdAndDeleteYn(memberId, 'N').orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다"));
        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        // 로그인에 성공하면 email, roles 로 토큰 생성 후 반환
        jwtTokenProvider.createToken(member.getMemberId(), "Access");

    }


    @Test
    @DisplayName("사용자 등록(회원가입)")
    public void create() {

        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(memberId);
        memberDto.setName(name);
        memberDto.setPassword(bCryptPasswordEncoder.encode(password));
        memberDto.setEmail(email);
        memberDto.setTelNum(telNum);
        memberDto.setRole(role);

        //when
        Member saveMember = memberRepository.save(memberMapper.toMemberEntity(memberDto));

        //then
        Assertions.assertThat(saveMember.getMemberId()).isEqualTo(memberDto.getMemberId());

    }

    @Test
    @DisplayName("사용자 정보 호출 테스트")
    public void userId() {

        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(memberId);
        memberDto.setName(name);
        memberDto.setPassword(bCryptPasswordEncoder.encode(password));
        memberDto.setEmail(email);
        memberDto.setTelNum(telNum);
        memberDto.setRole(role);

        Member saveMember = memberRepository.save(memberMapper.toMemberEntity(memberDto));

        Long id = saveMember.getId();

        //when
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));
        //then
        Assertions.assertThat(member.getId()).isEqualTo(id);

    }

    @Test
    @Transactional
    @DisplayName("사용자 정보 수정 테스트")
    void updateMember() {

        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(memberId);
        memberDto.setName(name);
        memberDto.setPassword(bCryptPasswordEncoder.encode(password));
        memberDto.setEmail(email);
        memberDto.setTelNum(telNum);
        memberDto.setRole(role);

        Member saveMember = memberRepository.save(memberMapper.toMemberEntity(memberDto));

        Long id = saveMember.getId();
        name = "김영수";

        //when
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));
        member.setName(name);

        //then
        assertThat(saveMember.getName()).isEqualTo(member.getName());
    }


    @Test
    @Transactional
    @DisplayName("패스워드 변경 테스트")
    void passwordUpdate() {

        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(memberId);
        memberDto.setName(name);
        memberDto.setPassword(bCryptPasswordEncoder.encode(password));
        memberDto.setEmail(email);
        memberDto.setTelNum(telNum);
        memberDto.setRole(role);

        Member saveMember = memberRepository.save(memberMapper.toMemberEntity(memberDto));
        Long id = saveMember.getId();

        String editPassword = "xptmxm!@34";
        String encodePassword = bCryptPasswordEncoder.encode(editPassword);

        //when
        Member member = memberRepository.findByIdAndDeleteYn(id, 'N').orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다."));
        member.setPassword(encodePassword);

        //then
        Assertions.assertThat(bCryptPasswordEncoder.matches(editPassword, encodePassword)).isTrue();
        Assertions.assertThat(member.getPassword()).isEqualTo(encodePassword);
    }

    @Test
    @Transactional
    @DisplayName("회원 삭제")
    void memberDelete() {
        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(memberId);
        memberDto.setName(name);
        memberDto.setPassword(bCryptPasswordEncoder.encode(password));
        memberDto.setEmail(email);
        memberDto.setTelNum(telNum);
        memberDto.setRole(role);

        Member saveMember = memberRepository.save(memberMapper.toMemberEntity(memberDto));
        Long id = saveMember.getId();


    }
}