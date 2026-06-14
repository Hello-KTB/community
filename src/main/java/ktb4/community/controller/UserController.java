package ktb4.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ktb4.community.global.code.SuccessCode;
import ktb4.community.dto.request.CreateUserRequestDto;
import ktb4.community.dto.request.UpdatePasswordRequestDto;
import ktb4.community.dto.request.UpdateUserRequestDto;
import ktb4.community.dto.response.ApiResponseDto;
import ktb4.community.dto.response.UpdateUserResponseDto;
import ktb4.community.service.ImageService;
import ktb4.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

// 회원 관련 HTTP 요청을 처리하는 컨트롤러
// /v1/users 하위 URI를 담당
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    /**
     * 회원가입
     * POST /v1/users
     * 토큰 불필요 (JwtAuthFilter EXCLUDED_PATHS에 포함)
     *
     * 요청 바디 : 이메일, 비밀번호, 닉네임, 프로필 이미지
     * 응답 : 201 Created
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto> create(@Valid @RequestBody CreateUserRequestDto dto) {
        userService.create(dto);
        return ResponseEntity
                .status(SuccessCode.SIGNUP_SUCCESS.getStatus())
                .body(ApiResponseDto.success(null, SuccessCode.SIGNUP_SUCCESS));
    }

    /**
     * 회원정보 수정 (닉네임, 프로필 이미지)
     * PATCH /v1/users/me
     * 토큰에서 추출한 userId로 본인 여부 확인
     *
     * 요청 바디 : 닉네임(필수), 프로필 이미지(선택)
     * 응답 : 200 OK, 수정된 닉네임과 프로필 이미지 반환
     */
    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto> update(HttpServletRequest request, @Valid @RequestBody UpdateUserRequestDto dto) {
        // 토큰에서 추출한 userId로 수정 대상 회원 식별
        Long userId = (Long) request.getAttribute("userId");
        UpdateUserResponseDto updatedUser = userService.update(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success(updatedUser, SuccessCode.UPDATE_USER_SUCCESS));
    }

    /**
     * 비밀번호 변경
     * PATCH /v1/users/me/password
     * 토큰에서 추출한 userId로 본인 여부 확인
     *
     * 요청 바디 : 새 비밀번호
     * 응답 : 200 OK
     */
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponseDto> updatePassword(HttpServletRequest request, @Valid @RequestBody UpdatePasswordRequestDto dto) {
        // 토큰에서 추출한 userId로 수정 대상 회원 식별
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePassword(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success(null, SuccessCode.UPDATE_PASSWORD_SUCCESS));
    }

    /**
     * 회원 탈퇴
     * DELETE /v1/users/me
     * 토큰에서 추출한 userId로 본인 여부 확인
     * DB CASCADE 설정으로 해당 회원의 게시글, 댓글, 좋아요도 함께 삭제됨
     *
     * 응답 : 204 No Content
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(HttpServletRequest request) {
        // 토큰에서 추출한 userId로 삭제 대상 회원 식별
        Long userId = (Long) request.getAttribute("userId");
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 이메일 중복 체크
     * GET /v1/users/email/check?email=...
     * 토큰 불필요 (JwtAuthFilter EXCLUDED_PATHS에 포함)
     * 회원가입 시 이메일 입력 단계에서 실시간으로 호출됨
     * 중복이면 UserService에서 409 예외 발생
     *
     * 응답 : 200 OK (사용 가능), 409 Conflict (중복)
     */
    @GetMapping("/email/check")
    public ResponseEntity<ApiResponseDto> checkEmail(@RequestParam String email) {
        userService.checkEmail(email);
        return ResponseEntity.ok(ApiResponseDto.success(null, SuccessCode.CHECK_EMAIL_SUCCESS));
    }

    /**
     * 닉네임 중복 체크
     * GET /v1/users/nickname/check?nickname=...
     * 토큰 불필요 (JwtAuthFilter EXCLUDED_PATHS에 포함)
     * 회원가입 시 닉네임 입력 단계에서 실시간으로 호출됨
     * 중복이면 UserService에서 409 예외 발생
     *
     * 응답 : 200 OK (사용 가능), 409 Conflict (중복)
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<ApiResponseDto> checkNickname(@RequestParam String nickname) {
        userService.checkNickname(nickname);
        return ResponseEntity.ok(ApiResponseDto.success(null, SuccessCode.CHECK_NICKNAME_SUCCESS));
    }

    @PostMapping("/upload/profile-image")
    public ResponseEntity<ApiResponseDto> uploadProfileImage(@RequestParam("profileImage") MultipartFile file) {
        String url = imageService.uploadImage(file, "PROFILE");

        return ResponseEntity.ok(ApiResponseDto.success(
                Map.of("profileImageUrl", url),
                SuccessCode.IMAGE_UPLOAD_SUCCESS));
    }
}