package nguyenthanhtung.datn.util.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import nguyenthanhtung.datn.constants.ContantApplication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private Key secretKey;

    @PostConstruct
    public void init() {
        // Load từ System properties
        String secret = System.getProperty("SECRET_KEY", "defaultSecretKey1234567890");
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());

    }

    /**
     * Tạo JWT với claims động từ key-value pairs
     *
     * @param claims Map chứa các thông tin tùy ý muốn nhúng vào token
     * @return token dạng String
     */
    public String createToken(Map<String, Object> claims, String validity) {
        Date now = new Date();
        long validityInMilliseconds = Long.parseLong(validity) * 24 * 60 * 60 * 1000;
        Date expiry = new Date(now.getTime() + validityInMilliseconds);
        Object userIdObj = claims.get(ContantApplication.ClaimUserID.toString());
        String subject = userIdObj != null ? userIdObj.toString() : "unknown";

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT token");
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
