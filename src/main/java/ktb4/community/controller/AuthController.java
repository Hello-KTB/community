package ktb4.community.controller;

import jakarta.servlet.http.HttpServletResponse;
import ktb4.community.dto.request.AuthRequestDto;
import ktb4.community.dto.response.AuthResponseDto;
import ktb4.community.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth/tokens")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody AuthRequestDto request, HttpServletResponse response) {
        String accessToken = authService.loginUser(request.getEmail(), request.getPassword(), response);

        if (accessToken == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(new AuthResponseDto(accessToken));
    }

    @DeleteMapping
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        authService.logoutUser(response);
        return ResponseEntity.ok().build();
    }
}
