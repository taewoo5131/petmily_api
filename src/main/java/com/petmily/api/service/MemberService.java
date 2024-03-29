package com.petmily.api.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MemberService {
    ResponseEntity Join(Map<String, Object> paramMap);

    ResponseEntity login(Map<String, Object> paramMap);

    ResponseEntity findById(Map<String, Object> paramMap);

    ResponseEntity logout(Map<String, Object> paramMap);

    String getRefreshToken(String requestPk);

    void test();
}
