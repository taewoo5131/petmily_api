package com.petmily.api.dto;

import com.petmily.api.entity.Member;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private Member member;

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
