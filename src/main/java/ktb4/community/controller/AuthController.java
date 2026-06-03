package ktb4.community.controller;

import jakarta.servlet.http.HttpServletResponse;
import ktb4.community.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth/tokens")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        String accessToken = authService.loginUser(request.getEmail(), request.getPassword(), response);

        if (accessToken == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        authService.logoutUser(response);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class AuthRequest {
        private String email;
        private String password;
    }
}
