package com.kiot.sample.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends Time{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 4, max = 100)
    private String memberId;

    @Column(length = 10, nullable = false)
    @Size(min = 2, max = 10)
    private String name;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 100, nullable = false)
    private String email;
    @Column(length = 20)
    private String telNum;
    @Column(length = 200)
    private String imageUrl; // 프로필 이미지
    @Column(length = 10)
    private String loginType;
    @Column(length = 20)
    private String birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(length = 1)
    @ColumnDefault("'N'")
    private Character deleteYn;

    @Builder
    public Member(Long id, String memberId, String name, String password, String email, String telNum, String imageUrl, String birthDate, String loginType, Role role, Character deleteYn) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telNum = telNum;
        this.imageUrl = imageUrl;
        this.birthDate = birthDate;
        this.loginType = loginType;
        this.role = role;
        this.deleteYn = deleteYn;
    }


    public void updateMember(String name, String email, String telNum){
        this.name = name;
        this.email = email;
        this.telNum = telNum;
    }

}
