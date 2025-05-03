package naughty.tuzamate.auth.jwt;//package naughty.tuzamate.auth.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.SecurityException;
//import lombok.extern.slf4j.Slf4j;
//import naughty.tuzamate.auth.jwt.error.JwtErrorCode;
//import naughty.tuzamate.auth.jwt.error.exception.JwtException;
//import naughty.tuzamate.user.domain.User;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//@Slf4j
//public class JwtProvider {
//
//    private SecretKey secretKey;
//    private long accessExpiration;
//    private long refreshExpiration;
//
//    public JwtProvider(@Value("${jwt.secret}") String secret,
//                       @Value("${jwt.token.access-expiration-time}") long accessExpiration,
//                       @Value("${jwt.token.refresh-expiration-time}") long refreshExpiration
//                       ) {
//        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
//        this.accessExpiration = accessExpiration;
//        this.refreshExpiration = refreshExpiration;
//    }
//
//    public String createAccessToken(User member) {
//        return createToken(member.getEmail(), this.accessExpiration);
//    }
//
//    public String createRefreshToken(User member) {
//        return createToken(member.getEmail(), this.refreshExpiration);
//    }
//
//    public String getUserId(String token) {
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", String.class);
//    }
//
//    public String getEmail(String token) {
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
//    }
//
//    public String createToken(String username, long expiration) {
//
//        return Jwts.builder()
//                .claim("role", "ROLE_USER")
//                .claim("email", username)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public Boolean isExpired(String token) {
//
//        try {
//            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
//                    .before(new Date());
//        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
//            throw new JwtException(JwtErrorCode.TOKEN_INVALID);
//        } catch (ExpiredJwtException e) {
//            throw new JwtException(JwtErrorCode.TOKEN_EXPIRED);
//        }
//    }
//
//
//}

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.auth.jwt.error.JwtErrorCode;
import naughty.tuzamate.domain.user.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import naughty.tuzamate.auth.jwt.error.exception.AuthException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private SecretKey secret;
    private long accessExpiration;
    private long refreshExpiration;

    // @Value: yml에서 해당 값을 가져오기 (아래의 YML의 값을 가져올 수 있음)
    public JwtProvider(@Value("${JWT_SECRET}") String secret, @Value("${ACCESS_EXPIRATION}") long accessExpiration, @Value("${REFRESH_EXPIRATION}") long refreshExpiration) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 가져온 문자열로 SecretKey 생성
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String createAccessToken(User user) {
        return createToken(user, this.accessExpiration);
    }

    public String createRefreshToken(User user) {
        return createToken(user, this.refreshExpiration);
    }

    public String createToken(User member, long expiration) {
        Instant issuedAt = Instant.now(); // 만들어진 시간을 현재 시간으로
        Instant expiredAt = issuedAt.plusMillis(expiration); // 만들어진 시간에 시간을 추가해 만료일 만들기
        return Jwts.builder()
                .setHeader(Map.of("alg", "HS256", "typ", "JWT")) // JWT header 설정
                .setSubject(member.getEmail()) // JWT의 Subject을 email로 설정
                .claim("id", member.getId()) // id 를 Claim으로 추가
                .setIssuedAt(Date.from(issuedAt)) // 만들어진 시간을 현재 시간으로 설정
                .setExpiration(Date.from(expiredAt)) // 유효기간 설정
                .signWith(secret, SignatureAlgorithm.HS256) // 암호화를 위한 sign 설정
                .compact(); // 만들기
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = getClaims(token); // Claim들 가져오기
            return claims.getBody().getExpiration().after(Date.from(Instant.now())); // 만료일이 지났는지 확인
        } catch (AuthException e) {
            log.error(e.getMessage());
            return false;
        } catch (Exception e) {
            log.error(e.getMessage() + ": 토큰이 유효하지 않습니다.");
            return false;
        }
    }

    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser() // parsing 하기 위해 builder를 가져옴
                    .setSigningKey(secret) // sign key 설정
                    .build()
                    .parseClaimsJws(token); // claim 가져오기
        } catch (Exception e) { // parsing하는 과정에서 sign key가 틀리는 등의 이유로 일어나는 Exception
            throw new AuthException(JwtErrorCode.TOKEN_INVALID);
            // JwtErrorCode와 AuthException은 GeneralException과 BaseErrorCode를 상속받아서 만들어주세요.
            // 저는 아래와 같이 Code를 만들었습니다.
            // INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "토큰이 유효하지 않습니다."),

        }
    }

    public String getEmail(String token) {
        return getClaims(token).getBody().getSubject();
    }

}
