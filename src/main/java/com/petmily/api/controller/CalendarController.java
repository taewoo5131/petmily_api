package com.petmily.api.controller;

import com.petmily.api.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity selectCalendar(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[CalendarController.selectCalendar] >> select");
        return calendarService.select(paramMap);
    }

    @PatchMapping
    public ResponseEntity updateCalendar(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[CalendarController.updateCalendar] >> update");
        return calendarService.update(paramMap);
    }

    @DeleteMapping
    public ResponseEntity deleteCalendar(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[CalendarController.deleteCalendar] >> delete");
        return calendarService.delete(paramMap);
    }

    @PostMapping("/check")
    public ResponseEntity checkCalendar(
            @RequestBody Map<String, Object> paramMap
    ) {
        log.info("[CalendarController.checkCalendar] >> check");
        return calendarService.check(paramMap);
    }
}
