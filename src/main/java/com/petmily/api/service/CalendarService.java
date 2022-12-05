package com.petmily.api.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CalendarService {
    ResponseEntity create(Map<String, Object> paramMap);
    ResponseEntity select(Map<String, Object> paramMap);
    ResponseEntity update(Map<String, Object> paramMap);
    ResponseEntity delete(Map<String, Object> paramMap);
    ResponseEntity check(Map<String, Object> paramMap);
}
