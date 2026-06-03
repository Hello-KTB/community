package ktb4.community.controller;

import ktb4.community.entity.User;
import ktb4.community.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        User saved = userService.create(request.email, request.password, request.nickname, request.image);
        return UserResponse.of(saved);
    }

    @GetMapping(("/{id}"))
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.of(userService.findById(id));
    }

    @PatchMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.update(id, request.nickname);
        return UserResponse.of(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @Data
    public static class UpdateUserRequest {
        private String nickname;
    }

    @Data
    public static class CreateUserRequest {
        private String email;
        private String password;
        private String nickname;
        private String image;
    }

    @Data
    public static class UserResponse {
        private Long id;
        private String email;
        private String nickname;

        public static UserResponse of(User user) {
            return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
        }

        public UserResponse(Long id, String email, String nickname) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
        }
    }
}