package ktb4.community.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey key;

    public JwtProvider(@Value("${spring.jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    // Access Token 생성 (15분)
    public String createAccessToken(Long userId) {
        long accessTtlSec = 30 * 60;
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key)
                .compact();
    }

    // Refresh Token 생성 (7일)
    public String createRefreshToken(Long userId) {
        long refreshTtlSec = 7 * 24 * 60 * 60;
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(refreshTtlSec)))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(jwt);
    }
}
