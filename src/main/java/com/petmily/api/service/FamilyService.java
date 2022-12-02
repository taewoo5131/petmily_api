package com.petmily.api.service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FamilyService {
    ResponseEntity create(Map<String, Object> paramMap);

    ResponseEntity regist(List<Map<String, Object>> paramList);

    ResponseEntity response(Map<String, Object> paramMap);
}
