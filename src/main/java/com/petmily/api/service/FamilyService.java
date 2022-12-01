package com.petmily.api.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface FamilyService {
    ResponseEntity create(Map<String, Object> paramMap);

    ResponseEntity regist(Map<String, Object> paramMap);
}
