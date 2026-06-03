package ktb4.community.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey key = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode("YWRhcHRlcnphZGFwdGVyemFkYXB0ZXJ6YWRhcHRlcnphZGFwdGVyeg==") // adapterzadapterzadapterzadapterzadapterz
    );

    public String createAccessToken(Long userId) {
        long accessTtlSec = 15 * 60;
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(jwt);
    }
}
