package ktb4.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import ktb4.community.dto.request.CreateUserRequestDto;
import ktb4.community.dto.request.UpdatePasswordRequestDto;
import ktb4.community.dto.request.UpdateUserRequestDto;
import ktb4.community.dto.response.ApiResponseDto;
import ktb4.community.dto.response.UpdateUserResponseDto;
import ktb4.community.entity.User;
import ktb4.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> create(@RequestBody CreateUserRequestDto request) {
        User saved = userService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(
                        HttpStatus.CREATED.value(),
                        true,
                        "회원가입에 성공하셨습니다",
                        null));
    }

    /*
    @GetMapping(("/{id}"))
    public ResponseEntity findById(@PathVariable Long id) {
        return ResponseEntity.status(201).build();
    }*/

    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto> update(HttpServletRequest request, @RequestBody UpdateUserRequestDto dto) {
        Long userId = (Long) request.getAttribute("userId");
        User updatedUser = userService.update(userId, dto.getNickname(), dto.getProfileImage());
        return ResponseEntity
                .status(200)
                .body(new ApiResponseDto<>(
                        200,
                        true,
                        "회원정보가 수정되었습니다",
                        new UpdateUserResponseDto(updatedUser.getNickname(), updatedUser.getProfileImage())
                ));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponseDto> updatePassword(HttpServletRequest request, @RequestBody UpdatePasswordRequestDto dto) {
        Long userId = (Long) request.getAttribute("userId");
        User updatedUser = userService.updatePassword(userId, dto.getPassword(), dto.getValidatePassword());
        return ResponseEntity
                .status(200)
                .body(new ApiResponseDto<>(
                        200,
                        true,
                        "비밀번호가 변경되었습니다",
                        null
                ));
    }

    @DeleteMapping("/me")
    public ResponseEntity delete(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}