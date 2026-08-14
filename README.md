# 🏘️ KTB4 Community

> **카카오테크 부트캠프 4기 — 클라우드 네이티브 개인 프로젝트**  
> Kubernetes 3-Tier 아키텍처 기반으로 배포 및 운영되는 게시판 커뮤니티 백엔드 서비스입니다.  
> Spring Boot 기반 RESTful API, Redis를 활용한 JWT Refresh Token 관리, Prometheus/Loki/Grafana 풀스택 모니터링 환경을 구축했습니다.

---

## 💡 프로젝트 개요

- **프로젝트명**: 커뮤니티 웹 애플리케이션
- **개발자**: peter.jung(정광혁)
- **개발 기간**: 2026.05 ~ 2026.08
- **개발 인원**: 1명 (프론트엔드 + 백엔드 + 인프라)

### 주요 목표

- Spring Boot와 MySQL/Redis를 이용한 안정적인 게시판 RESTful API 구현
- JWT (Access Token + Refresh Token) 기반 인증 및 Redis 연동을 통한 토큰 관리
- Docker 멀티 스테이지 빌드 및 Kubernetes 3-Tier 아키텍처 배포
- Helm + ArgoCD GitOps 기반 배포 자동화
- Prometheus, Loki, Grafana를 연동한 실시간 메트릭 & 로그 통합 모니터링 구축

---

## 🎬 서비스 시연 영상

[> 시연 영상](https://drive.google.com/file/d/13LUqWx_8h5lNPNGop6hy7RL_uPvafIhl/view?usp=drive_link)

---

## ✨ 주요 기능

### 🔐 회원 인증 (Auth)

- JWT 기반 로그인 / 회원가입 — Access Token (30분), Refresh Token (7일) 발급
- Redis 기반 Refresh Token 관리 — 로그인 시 저장, 로그아웃 시 무효화
- Access Token 만료 시 Refresh Token으로 자동 재발급

### 👤 회원 관리 (User)

- 회원정보 수정 (닉네임, 프로필 이미지)
- 비밀번호 변경
- 회원 탈퇴

### 📝 게시글 (Post)

- 게시글 작성, 조회, 수정, 삭제
- AWS S3 이미지 업로드 연동
- 최신순 / 인기순 정렬 및 검색

### 💬 댓글 (Comment)

- 게시글별 댓글 작성, 수정, 삭제

### 📊 모니터링 (Observability)

- Prometheus + Grafana — JVM, CPU, Memory, HTTP Request 메트릭 실시간 시각화
- Loki + Promtail — Kubernetes 파드 로그 실시간 수집 및 Grafana 대시보드 조회

---

## 🛠 기술 스택

### Backend

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Build Tool | Gradle |
| Database | MySQL 8.0, Spring Data JPA |
| Cache / Auth | Redis (Alpine), Spring Security, JJWT |

### DevOps / Infrastructure

| 분류 | 기술 |
|---|---|
| Container | Docker (Multi-stage build) |
| Orchestration | Kubernetes (Control Plane + Worker Node × 2) |
| GitOps | ArgoCD, Helm |
| Ingress | Nginx Ingress Controller |
| DNS | AWS Route53 |
| Storage | AWS S3 |

### Monitoring

| 분류 | 기술 |
|---|---|
| Metrics | Prometheus (kube-prometheus-stack) |
| Logs | Grafana Loki (loki-stack), Promtail |
| Visualization | Grafana |

---

## 시스템 아키텍처

<img width="1682" height="1080" alt="community_system_architecture" src="https://github.com/user-attachments/assets/5680d771-9152-4a51-8679-2a6dd4a662e7" />

### 트래픽 흐름

```
User
└── Route53 (DNS)
    └── NAT & Bastion (Public Subnet)
        └── Nginx Ingress Controller (NodePort :30816)
            ├── /api/** → Backend Pod (Spring Boot :8080)
            │   ├── MySQL (DB 조회)
            │   ├── Redis (Refresh Token 조회)
            │   └── AWS S3 (이미지 업로드)
            └── /**    → Frontend Pod (Next.js :3000)
```

### GitOps 배포 흐름

```
개발자 (git push)
└── GitHub (k8s-manifest repo)
    └── ArgoCD (auto-sync)
        └── Kubernetes Cluster (deploy)
            └── Helm Chart (backend / frontend / ingress / monitoring)
```

---

## 🗂 서버 설계

### 폴더 구조

```
src/
└── main/
    ├── java/ktb4/community/
    │   ├── config/
    │   │   ├── CorsConfig
    │   │   ├── QueryDSLConfig
    │   │   ├── RedisConfig
    │   │   ├── SecurityConfig
    │   │   ├── SeedConfig
    │   │   └── WebConfig
    │   ├── controller/
    │   │   ├── AuthController
    │   │   ├── HealthController
    │   │   ├── PostController
    │   │   └── UserController
    │   ├── dto/
    │   │   ├── request/
    │   │   │   ├── CommentRequestDto
    │   │   │   ├── CreatePostRequestDto
    │   │   │   ├── CreateUserRequestDto
    │   │   │   ├── LoginRequestDto
    │   │   │   ├── UpdatePasswordRequestDto
    │   │   │   ├── UpdatePostRequestDto
    │   │   │   └── UpdateUserRequestDto
    │   │   └── response/
    │   │       ├── ApiResponseDto
    │   │       ├── AuthorResponseDto
    │   │       ├── CommentResponseDto
    │   │       ├── CreatePostResponseDto
    │   │       ├── LoginResponseDto
    │   │       ├── PostDetailResponseDto
    │   │       ├── PostLikeResponseDto
    │   │       ├── PostSummaryResponseDto
    │   │       ├── PresignedUrlResponseDto
    │   │       ├── UpdatePostResponseDto
    │   │       └── UpdateUserResponseDto
    │   ├── entity/
    │   │   ├── Comment
    │   │   ├── Image
    │   │   ├── Post
    │   │   ├── PostLike
    │   │   ├── PostLikeId
    │   │   └── User
    │   ├── filter/
    │   │   └── JwtAuthFilter
    │   ├── global/
    │   │   ├── code/
    │   │   │   ├── ErrorCode
    │   │   │   └── SuccessCode
    │   │   └── exception/
    │   │       ├── CustomException
    │   │       └── GlobalExceptionHandler
    │   ├── jwt/
    │   │   └── JwtProvider
    │   ├── repository/
    │   │   ├── CommentRepository
    │   │   ├── ImageRepository
    │   │   ├── PostLikeRepository
    │   │   ├── PostRepository
    │   │   ├── PostRepositoryCustom
    │   │   ├── PostRepositoryImpl
    │   │   └── UserRepository
    │   ├── service/
    │   │   ├── AuthService
    │   │   ├── CommentService
    │   │   ├── ImageService
    │   │   ├── PostLikeService
    │   │   ├── PostService
    │   │   └── UserService
    │   └── CommunityApplication
    └── resources/
        ├── static/
        ├── templates/
        ├── application.yaml
        ├── application-local.yaml
        └── application-prod.yaml
```

### API 구조



---

## 🗄 데이터베이스 설계

### ERD

> 추후 이미지 수정 예정

<img width="792" height="549" alt="스크린샷 2026-08-09 오후 11 24 33" src="https://github.com/user-attachments/assets/92118083-86e9-4fa4-b970-916b6e2e1286" />


## 🔧 트러블슈팅

> 추후 작성

---

## 프로젝트 후기

