package com.petmily.api.service;

import com.petmily.api.dto.TokenDTO;
import com.petmily.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDTO createToken(String memberSeqId) {
        String accessJwtToken = jwtTokenProvider.makeAccessJwtToken(memberSeqId);
        String refreshJwtToken = jwtTokenProvider.makeRefreshJwtToken();
        return new TokenDTO(accessJwtToken, refreshJwtToken);
    }
}
