package com.petmily.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
