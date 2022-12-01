package com.petmily.api.controller;

import com.petmily.api.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/family")
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    public ResponseEntity createFamily(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[FamilyController.createFamily] >> create");
        return familyService.create(paramMap);
    }

    @PostMapping("/registration")
    public ResponseEntity registryFamily(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[FamilyController.registryFamily] >> regist");
        return familyService.regist(paramMap);
    }
}
