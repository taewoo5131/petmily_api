package com.petmily.api.security;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
//    private String secretMsg = "petmilyapiproject2022";
//    private String secretKey = Base64.getEncoder().encodeToString(secretMsg.getBytes());

    @Value("${spring.jwt.key}")
    private String secretKey;
    // accessToken 30분
    private final long accessTokenValidTime = 30 * 60 * 1000L;

    // resfreshToken 2주
    LocalDate today = LocalDate.now();
    LocalDate next2Week = today.plus(2, ChronoUnit.WEEKS);
    Instant instant = next2Week.atStartOfDay(ZoneId.systemDefault()).toInstant();
    private final Date refreshTokenVaildTime = Date.from(instant);

    @PostConstruct
    void init() {
        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

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
//                .setExpiration(new Date(20 * 1000L)) // 20초
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }
}
