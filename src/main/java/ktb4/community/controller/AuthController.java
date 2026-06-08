package ktb4.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ktb4.community.dto.request.LoginRequestDto;
import ktb4.community.dto.response.ApiResponseDto;
import ktb4.community.dto.response.AuthorResponseDto;
import ktb4.community.dto.response.LoginResponseDto;
import ktb4.community.entity.User;
import ktb4.community.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 인증 관련 HTTP 요청을 처리하는 컨트롤러
// /v1/auth 하위 URI를 담당 (로그인, 로그아웃, 로그인 상태 확인)
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인
     * POST /v1/auth/tokens
     * 토큰 불필요 (JwtAuthFilter EXCLUDED_PATHS에 포함)
     * 인증 성공 시 Access Token을 HttpOnly 쿠키에 담아 응답
     *
     * 요청 바디 : 이메일, 비밀번호
     * 응답 : 200 OK + Access Token, 인증 실패 시 401 Unauthorized
     */
    @PostMapping("/tokens")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {
        String accessToken = authService.loginUser(request.getEmail(), request.getPassword(), response);

        // 이메일 없거나 비밀번호 불일치 시 401 반환
        if (accessToken == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(new LoginResponseDto(accessToken));
    }

    /**
     * 로그아웃
     * DELETE /v1/auth/tokens
     * Access Token 쿠키를 즉시 만료시켜 클라이언트에서 삭제
     *
     * 응답 : 200 OK
     */
    @DeleteMapping("/tokens")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        authService.logoutUser(response);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그인 상태 확인
     * GET /v1/auth/check
     * 토큰 불필요 (JwtAuthFilter EXCLUDED_PATHS에 포함)
     * 프론트엔드가 페이지 진입 시마다 호출해서 로그인 상태를 확인
     * 토큰이 있고 유효하면 JwtAuthFilter에서 userId를 request에 저장
     * 토큰이 없거나 만료됐으면 userId가 null → 401 반환
     *
     * 응답 : 200 OK (로그인 상태), 401 Unauthorized (미로그인)
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponseDto> check(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponseDto<>(401, false, "로그인이 필요합니다", null));
        }
        User user = authService.getUser(userId);
        return ResponseEntity.ok()
                .body(new ApiResponseDto<>(200, true, "로그인 상태입니다",
                        new AuthorResponseDto(user.getNickname(), user.getProfileImage())
                ));
    }
}