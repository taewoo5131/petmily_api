package com.petmily.api.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CalendarService {
    ResponseEntity create(Map<String, Object> paramMap);
}
