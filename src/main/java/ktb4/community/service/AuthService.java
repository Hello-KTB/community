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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60; // 15분

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public String loginUser(String email, String password, HttpServletResponse response) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !checkPassword(user, password)) {
            return null;
        }

        String token =jwtProvider.createAccessToken(user.getId());
        addTokenCookie(response, "accessToken", token, ACCESS_TOKEN_EXPIRATION);
        return token;
    }

    public void logoutUser(HttpServletResponse response) {
        // 쿠키 즉시 만료
        addTokenCookie(response, "accessToken", null, 0);
    }

    /** 공통 쿠키 생성 로직 */
    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public record TokenResponse(String accessToken) {}
}