package com.petmily.api.interceptor;

import com.petmily.api.Exception.custom.TokenException;
import com.petmily.api.common.RedisUtil;
import com.petmily.api.security.JwtTokenProvider;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[AuthInterceptor.prehandle] >> {} " , request.getRequestURI());
        String requestToken = "";
        requestToken = request.getHeader("X-AUTH-TOKEN");
        if (requestToken != null && !requestToken.isEmpty()) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

            try {
                // 블랙리스트 체크
                Claims claims = Jwts.parser()
                        .setSigningKey(Base64.getEncoder().encodeToString("petmilyapiproject2022".getBytes()))
                        .parseClaimsJws(requestToken)
                        .getBody();
                if (redisUtil.hasKey("member_"+claims.get("pk"))) {
                    throw new TokenException("blackList token");
                }

                // accessToken check
                boolean tokenCheck = jwtTokenProvider.validAccessToken(requestToken);
                if (tokenCheck) {
                    return true;
                }
            // 인증기간 만료
            } catch (ExpiredJwtException e) {
                String requestPk = String.valueOf(e.getClaims().get("pk"));
                response.sendRedirect(request.getContextPath() + "/member/refresh-token?pk="+requestPk);
            // 인증 오류
            } catch (SignatureException e) {
                throw new TokenException("token 누락");
            }
        } else {
            throw new TokenException("token 누락");
        }
        return false;
    }
}
