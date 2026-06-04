package ktb4.community.controller;

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

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto> update(@PathVariable Long id, @RequestBody UpdateUserRequestDto request) {
        User updatedUser = userService.update(id, request.getNickname(), request.getProfileImage());
        return ResponseEntity
                .status(200)
                .body(new ApiResponseDto<>(
                        200,
                        true,
                        "회원정보가 수정되었습니다",
                        new UpdateUserResponseDto(updatedUser.getNickname(), updatedUser.getProfileImage())
                ));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponseDto> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequestDto request) {
        User updatedUser = userService.updatePassword(id, request.getPassword(), request.getValidatePassword());
        return ResponseEntity
                .status(200)
                .body(new ApiResponseDto<>(
                        200,
                        true,
                        "회원정보가 수정되었습니다",
                        new UpdateUserResponseDto(updatedUser.getNickname(), updatedUser.getProfileImage())
                ));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}