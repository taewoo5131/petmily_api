package com.petmily.api.service.impl;

import com.petmily.api.common.PetmilyUtil;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.entity.*;
import com.petmily.api.repository.CalendarNotiRepository;
import com.petmily.api.repository.CalendarRepository;
import com.petmily.api.repository.FamilyRepository;
import com.petmily.api.service.CalendarService;
import com.petmily.api.service.MemberCalendarNotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarNotiRepository calendarNotiRepository;
    private final FamilyRepository familyRepository;
    private final MemberCalendarNotiService memberCalendarNotiService;

    @Override
    @Transactional
    public ResponseEntity create(Map<String, Object> paramMap) {
        log.info("[CalendarServiceImpl create]");
        String[] checkKeyArr = {"memberIdx","familyIdx", "target_date", "target_name"};
        if (!PetmilyUtil.parameterNullCheck(paramMap , checkKeyArr)) {
            throw new IllegalArgumentException("CalendarServiceImpl.create 필수값 누락");
        }

        Family findFamily = familyRepository.findFamilyByIdx(paramMap.get("familyIdx").toString());
        if (findFamily == null) {
            throw new IllegalArgumentException("CalendarServiceImpl.create 필수값 누락");
        }

        /**
         * Calendar에 등록
         */
        Calendar calendar = new Calendar();
        calendar.setFamily(findFamily);
        calendar.setTargetDate(paramMap.get("target_date").toString());
        calendar.setTargetName(paramMap.get("target_name").toString());
        Calendar saveCalendar = calendarRepository.save(calendar);
        if (saveCalendar == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /**
         * CalendarNoti에 등록
         */
        CalendarNoti calendarNoti = new CalendarNoti();
        calendarNoti.setCalendar(saveCalendar);
        CalendarNoti saveCalendarNoti = calendarNotiRepository.save(calendarNoti);
        if (saveCalendarNoti == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /**
         * MemberCalendarNoti에 등록
         */
        List<MemberCalendarNoti> saveMemberCalendarNotiList = memberCalendarNotiService.create(paramMap.get("memberIdx").toString(), findFamily, saveCalendarNoti);
        if (saveMemberCalendarNotiList.size() <= 0) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(calendar);
        return new ResponseEntity(successResponse, HttpStatus.OK);
    }
}
