package com.petmily.api.service;

import com.petmily.api.dto.TokenDTO;
import com.petmily.api.security.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;

    public String createAccessToken(String memberSeqId) {
        return jwtTokenProvider.makeAccessJwtToken(memberSeqId);
    }

    public String createRefreshToken() {
        return jwtTokenProvider.makeRefreshJwtToken();
    }

    public TokenDTO createToken(String memberSeqId) {
        String accessJwtToken = jwtTokenProvider.makeAccessJwtToken(memberSeqId);
        String refreshJwtToken = jwtTokenProvider.makeRefreshJwtToken();
        return new TokenDTO(accessJwtToken, refreshJwtToken);
    }

    public boolean tokenCheck(String token) {
        try {
            boolean b = jwtTokenProvider.validAccessToken(token);
            if(b){
                return true;
            } else {
                return false;
            }
        } catch (ExpiredJwtException e) {
            return false;
        }
    }


}
