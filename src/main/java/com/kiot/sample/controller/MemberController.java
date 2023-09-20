package com.kiot.sample.controller;

import com.kiot.sample.domain.Member;
import com.kiot.sample.dto.member.MemberDto;
import com.kiot.sample.dto.member.MemberUpdateDto;
import com.kiot.sample.dto.member.PasswordDto;
import com.kiot.sample.dto.ResponseDto;
import com.kiot.sample.mapper.MemberMapper;
import com.kiot.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

/*    @GetMapping("/idChk")
    public ResponseEntity<ResponseDto> idCheck(@RequestBody String memberId) {

        ResponseDto<Object> responseDto;

        //todo - 아이디 중복 체크
        boolean checkMemberId = memberService.checkMemberId(memberId);

        if (checkMemberId != true) {
            responseDto = ResponseDto.builder()
                    .code("F002")
                    .message("사용 중인 아이디압니다")
                    .data(null)
                    .build();
        } else {
            responseDto = ResponseDto.builder()
                    .code("S002")
                    .message("사용 가능한 아이디압니다")
                    .data(null)
                    .build();

        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }*/


    @GetMapping("/idChk")
    public ResponseEntity<Boolean> idCheck(@RequestParam String memberId) {
        return ResponseEntity.ok(memberService.checkMemberId(memberId));
    }

    @PostMapping()
    public ResponseEntity<ResponseDto> signup(@RequestBody @Validated MemberDto memberDto) {

        Member member = memberMapper.toMemberEntity(memberDto);
        memberService.signup(memberDto);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("회원가입에 성공하였습니다")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> selectMember(@PathVariable(value = "id") Long id) {

        MemberDto memberDto = memberService.selectMember(id);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message(memberDto.getMemberId() + "님 조회에 성공하였습니다.")
                .data(Arrays.asList(memberDto))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<ResponseDto> getUpdateMember(@PathVariable(value = "id") Long id) {

        MemberDto memberDto = memberService.selectMember(id);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message(memberDto.getMemberId() + "님 조회에 성공하였습니다.")
                .data(Arrays.asList(memberDto))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateMember(@PathVariable(value = "id") Long id, @RequestBody @Validated MemberUpdateDto memberUpdateDto) {

        //사용자정보 가져와서 저장
        memberService.update(id, memberUpdateDto);

        //ResponseDto 리턴
        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("회원정보 수정을 성공하였습니다.")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PutMapping("/password/{id}")
    public ResponseEntity<ResponseDto> updatePassword(@PathVariable(value = "id") Long id, @RequestBody @Validated PasswordDto passwordDto) {

        //이전 패스워드 맞는지 확인 & 패스워드가져와서 저장
        memberService.updatePassword(id, passwordDto);

        //ResponseDto 리턴
        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("패스워드 변경 완료하였습니다.")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteMember(@PathVariable(value = "id") Long id) {
        //todo - 삭제 기능
        memberService.deleteMember(id);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("회원 탈퇴에 성공하였습니다.")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto> selectAll() {
        List<Member> members = memberService.selectMemberAll();

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .code("S002")
                .message("모든 사용자 조회에 성공하였습니다.")
                .data(new ArrayList<>(members))
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
