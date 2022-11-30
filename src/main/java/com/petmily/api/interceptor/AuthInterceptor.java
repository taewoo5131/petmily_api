package com.petmily.api.interceptor;

import com.petmily.api.Exception.custom.TokenException;
import com.petmily.api.security.JwtTokenProvider;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[AuthInterceptor.prehandle] >> {} " , request.getRequestURI());
        String requestToken = "";
        requestToken = request.getHeader("X-AUTH-TOKEN");
        if (requestToken != null && !requestToken.isEmpty()) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
            // accessToken check
            try {
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
                // 로그아웃 후 재로그인
                return false;
            }
        }
        throw new TokenException("token 누락");
    }
}
