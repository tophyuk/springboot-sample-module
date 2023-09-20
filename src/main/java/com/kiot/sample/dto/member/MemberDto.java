package com.kiot.sample.dto.member;

import com.kiot.sample.auth.OAuth2UserInfo;
import com.kiot.sample.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class MemberDto {

    @NotBlank(message = "사용자 명은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{4,12}$", message = "아이디는 특수문자를 제외한 4~12자리여야 합니다.")
    private String memberId;
    private String name;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    private String telNum;
    private String imageUrl; // 프로필 이미지
    private String loginType;
    private String birthDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Character deleteYn;

    public MemberDto(String memberId, String name, String password, String email, String telNum, String imageUrl, String loginType, String birthDate, Role role, Character deleteYn) {
        this.memberId = memberId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telNum = telNum;
        this.imageUrl = imageUrl;
        this.loginType = loginType;
        this.birthDate = birthDate;
        this.role = role;
        this.deleteYn = deleteYn;
    }
}
