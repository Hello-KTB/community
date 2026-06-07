package ktb4.community.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ktb4.community.entity.User;
import ktb4.community.jwt.JwtProvider;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 인증 관련 비즈니스 로직을 처리하는 서비스 계층
// 로그인, 로그아웃, 토큰 발급을 담당
@Service
@RequiredArgsConstructor
// 기본적으로 읽기 전용 트랜잭션 적용 (SELECT 최적화)
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // Access Token 만료 시간 (15분)
    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60;

    /**
     * ID로 회원 조회
     * /v1/auth/check에서 로그인 상태 확인 시 사용
     *
     * 파라미터 : userId 조회할 회원 ID
     * 반환 : 조회된 User 엔티티, 존재하지 않으면 null
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * 로그인
     * 이메일로 회원을 조회하고 비밀번호 검증 후 Access Token 발급
     * 발급된 토큰은 HttpOnly 쿠키에 담아 응답 (XSS 공격 방지)
     * Refresh Token 없이 Access Token만 사용 (stateless 유지)
     *
     * 파라미터 : email    로그인 이메일
     * 파라미터 : password 로그인 비밀번호 (평문)
     * 파라미터 : response 쿠키를 담을 HTTP 응답 객체
     * 반환 : 발급된 Access Token 문자열, 인증 실패 시 null
     */
    public String loginUser(String email, String password, HttpServletResponse response) {
        // 이메일로 회원 조회
        User user = userRepository.findByEmail(email).orElse(null);

        // 회원이 없거나 비밀번호 불일치 시 null 반환 → Controller에서 401 처리
        if (user == null || !checkPassword(user, password)) {
            return null;
        }

        // userId를 담은 Access Token 생성
        String token = jwtProvider.createAccessToken(user.getId());
        // 토큰을 HttpOnly 쿠키에 담아 응답
        addTokenCookie(response, "accessToken", token, ACCESS_TOKEN_EXPIRATION);
        return token;
    }

    /**
     * 로그아웃
     * Access Token 쿠키를 즉시 만료시켜 클라이언트에서 삭제
     * maxAge를 0으로 설정하면 브라우저가 즉시 쿠키를 삭제
     *
     * 파라미터 : response 쿠키를 담을 HTTP 응답 객체
     */
    public void logoutUser(HttpServletResponse response) {
        // maxAge=0으로 설정해서 쿠키 즉시 만료
        addTokenCookie(response, "accessToken", null, 0);
    }

    /**
     * 쿠키 생성 공통 로직
     * HttpOnly 설정으로 JavaScript에서 접근 불가 (XSS 공격 방지)
     * Path를 "/"로 설정해서 모든 경로에서 쿠키 전송
     *
     * 파라미터 : response HTTP 응답 객체
     * 파라미터 : name    쿠키 이름
     * 파라미터 : value   쿠키 값 (로그아웃 시 null)
     * 파라미터 : maxAge  쿠키 유효 시간 (초), 0이면 즉시 만료
     */
    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 비밀번호 검증
     * 입력된 평문 비밀번호와 DB에 저장된 BCrypt 암호화 비밀번호를 비교
     *
     * 파라미터 : user        비밀번호를 검증할 User 엔티티
     * 파라미터 : rawPassword 입력된 평문 비밀번호
     * 반환 : 일치하면 true, 불일치하면 false
     */
    private boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Access Token을 담는 응답 레코드
    public record TokenResponse(String accessToken) {}
}