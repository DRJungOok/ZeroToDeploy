# AGENTS.md

## 🎯 프로젝트 개요

**ZeroToDeploy**는 Spring Boot 기반의 커뮤니티 및 사용자 관리 플랫폼입니다.  
기본 기능으로는 사용자 회원가입, 로그인, 친구 추가 및 미리보기, 게시판 작성/조회/분류, 마이페이지 수정, OAuth 로그인, 세션 처리, HTML 렌더링 기반 뷰 페이지가 포함됩니다.

---

## 🗂️ 패키지 구조 및 역할 설명

### 🔐 `config`
- 전반적인 Spring Security 설정 및 인증 필터 정의가 위치합니다.

### 👤 `details`
- `CustomUserDetails`, `CustomUserDetailsService`는 Spring Security에서 사용자 인증을 위한 커스텀 구현체입니다.
- 로그인 시 사용자 정보를 Spring Security에 연결하기 위해 사용됩니다.

### 📦 `joinMember`
- 회원 가입 및 로그인 기능 담당
- 주요 클래스:
    - `JoinUserController`: 회원가입 및 로그인 요청 처리
    - `JoinUserService`: 사용자 생성, 로그인 로직
    - `JoinUserEntity`: 사용자 JPA 엔티티
    - `JoinUserDTO`: 회원가입/로그인 시 사용되는 DTO
    - `JoinUserRepo`: 사용자 데이터 접근

### 🧑‍🤝‍🧑 `friends`
- 친구 요청/수락/목록 기능 처리 예정

### ⚙️ `myInfo`
- 사용자 마이페이지 기능 (닉네임, 이메일 등 수정)
- `MyInfoController`: 본인 정보 수정/업로드 기능 제공

### 📸 `preview`
- 다른 사용자 정보 미리보기 및 사용자 정보 조회
- `UserPreviewController`: 프로필 간단 조회, URL 기반 사용자 프로필 뷰 처리

### 💬 `comment`
- 댓글 관련 기능 담당 (파일은 비어있거나 생략되어 있음)

### ❤️ `like`
- 좋아요 기능 담당 (파일은 비어있거나 생략되어 있음)

### 📝 `post`
- 게시글 CRUD, 카테고리 분류 기능 담당
- 글쓰기, 수정, 삭제, 좋아요, 상세보기 포함

### 🛡️ `session`
- 세션 처리 기능 포함
- 사용자 세션 만료 및 타임아웃 처리 로직 포함 예상

---

## 🧾 템플릿 (HTML) 구조

### 📂 `/templates/`
- Thymeleaf 기반 HTML 뷰 템플릿

| 파일명 | 설명 |
|--------|------|
| `index.html` | 홈 화면 |
| `login.html`, `signUp.html` | 로그인/회원가입 |
| `postDetail.html`, `write.html` | 게시글 상세 / 글쓰기 |
| `friends.html` | 친구 목록 |
| `chat.html` | 채팅 기능 UI |
| `myInfo.html` | 사용자 정보 수정 |
| `web.html`, `javaSpring.html`, `linux.html` | 카테고리별 게시글 |
| `about.html`, `etc.html`, `history.html` | 기타 정적 페이지 |
| `common.html` | 공통 레이아웃 조각 |
| `error/notFound.html` | 404 등 오류 처리 페이지 |

---

## 🔐 보안 구조

- `CustomUserDetails` 및 `CustomUserDetailsService`는 Spring Security에서 사용자 인증을 처리
- JWT 또는 세션 기반 인증과 연동 가능
- 관리자/사용자 구분을 `ROLE_ADMIN`, `ROLE_USER` 기반으로 처리 가능

---

## 🔄 요청 흐름 예시

```plaintext
1. 사용자가 `/login` → JoinUserController.login()
2. 인증 → CustomUserDetailsService.loadUserByUsername()
3. 성공 시 세션 저장 또는 JWT 발급
4. 이후 사용자는 게시판, 친구 등 기능 접근
5. 게시글 작성 시 PostController 호출
