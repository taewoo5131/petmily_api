package com.petmily.api.controller;

import com.petmily.api.service.CalendarService;
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
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping
    public ResponseEntity createCalendar(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[CalendarController.createCalendar] >> create");
        return calendarService.create(paramMap);
    }
}
