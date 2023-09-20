# springboot-sample-module
Springboot + spring security + jwt + Oauth2.0 + REST API

## 🖥️ 프로젝트 소개
두 번째 스프링부트 개인 프로젝트로서 이번엔 CSR을 위해 Thymeleaf를 사용하지 않고 백엔드와 프론트엔드를 완전히 구별하여 구현하였습니다.
첫 프로젝트에서 session을 이용한 로그인 구현을 하였다면, 이번에는 jwt를 통한 로그인을 구현하였으며, OAuth 역시 jwt를 통한 로그인을 구현하였습니다.

게시판 부분은 파일업로드 기능까지 추가하여 기본적인 기능을 포함시켜 모듈로서 역할을 하기 위해 만들었습니다.

<br>

## 🕰️ 개발 기간
* 23.02.01 - 23.2.20

### 🧑‍🤝‍🧑 맴버구성
 - 팀원1 : tophyuk - 개발 전체

### ⚙️ 개발 환경
- `Java 17`
- **IDE** : IntelliJ
- **Framework** : Springboot(3.1)
- **Database** : MySQL
- **ORM** : JPA

## 📌 주요 기능
#### 로그인
- 아이디 및 패스워드 DB값 검증
- 로그인 시 jwt 생성
- accessToken 만료 시 refreshToken을 통해 접근
- OAuth로 로그인 시에 최초는 회원가입, 기존에 존재하면 로그인
#### 회원가입
- 아이디 중복 체크
- 비밀번호, 비밀번호 확인 매칭
#### 게시판
- JPA Pageable을 통한 페이지 정보 전달
- 검색 조건 유지
- 파일 업로드 기능 추가
  
