package com.petmily.api.security;

import com.petmily.api.service.TokenService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secretKey = Base64.getEncoder().encodeToString("petmilyapiproject2022".getBytes());

    // accessToken 30분
//    private final long accessTokenValidTime = 30 * 60 * 1000L;
    private final long accessTokenValidTime = 10 * 1000L;

    // resfreshToken 2주
    LocalDate today = LocalDate.now();
    LocalDate next2Week = today.plus(2, ChronoUnit.WEEKS);
    Instant instant = next2Week.atStartOfDay(ZoneId.systemDefault()).toInstant();
    private final Date refreshTokenVaildTime = Date.from(instant);

    // JWT Access 토큰 생성
    public String makeAccessJwtToken(String memberSeqId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // 30분
                .claim("pk", memberSeqId)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    // JWT Refresh 토큰 생성
    public String makeRefreshJwtToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(refreshTokenVaildTime) // 2주
//                .setExpiration(new Date( * 1000L)) // 20초
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    // access 토큰 검증 + 만료시간 확인
    public boolean validAccessToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }
}
