package com.petmily.api.service.impl;

import com.petmily.api.common.PetmilyUtil;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.dto.MemberCalendarSelectDTO;
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
        log.info("여기 {} " , paramMap);
        String[] checkKeyArr = {"memberIdx","familyIdx", "targetDate", "targetName"};
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
        calendar.setTargetDate(paramMap.get("targetDate").toString());
        calendar.setTargetName(paramMap.get("targetName").toString());
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

    @Override
    public ResponseEntity select(Map<String, Object> paramMap) {
        log.info("[CalendarServiceImpl select]");
        String[] checkKeyArr = {"memberIdx", "familyIdx" , "searchStartDate" , "searchEndDate"};
        if (!PetmilyUtil.parameterNullCheck(paramMap, checkKeyArr)) {
            throw new IllegalArgumentException("CalendarServiceImpl.select 필수값 누락");
        }

        List<MemberCalendarSelectDTO> resultList = memberCalendarNotiService.select(paramMap);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(resultList);

        return new ResponseEntity(successResponse , HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Map<String, Object> paramMap) {
        log.info("[CalendarServiceImpl update]");
        String[] checkKeyArr = {"calendarIdx","memberIdx","familyIdx", "targetDate", "targetName"};
        if (!PetmilyUtil.parameterNullCheck(paramMap, checkKeyArr)) {
            throw new IllegalArgumentException("CalendarServiceImpl.update 필수값 누락");
        }

        Long updateIdx = calendarRepository.update(paramMap);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(Map.of("calendarIdx" , updateIdx));
        return new ResponseEntity(successResponse , HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity delete(Map<String, Object> paramMap) {
        log.info("[CalendarServiceImpl delete]");
        String[] checkKeyArr = {"familyIdx" , "calendarIdx"};
        if (!PetmilyUtil.parameterNullCheck(paramMap, checkKeyArr)) {
            throw new IllegalArgumentException("CalendarServiceImpl.delete 필수값 누락");
        }

        Long deleteCalendarIdx = calendarRepository.delete(paramMap);
        CalendarNoti findCalendarNoti = calendarNotiRepository.findByCalendarIdx(deleteCalendarIdx);
        Long deleteCalendarNotiIdx = calendarNotiRepository.delete(findCalendarNoti);
        memberCalendarNotiService.delete(deleteCalendarNotiIdx);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(Map.of("calendarIdx" , deleteCalendarIdx));
        return new ResponseEntity(successResponse , HttpStatus.OK);
    }

    @Override
    public ResponseEntity check(Map<String, Object> paramMap) {
        log.info("[CalendarServiceImpl check]");
        String[] checkKeyArr = {"memberIdx" , "calendarIdx" , "checkYn"};
        if (!PetmilyUtil.parameterNullCheck(paramMap, checkKeyArr)) {
            throw new IllegalArgumentException("CalendarServiceImpl.check 필수값 누락");
        }

        MemberCalendarNoti update = memberCalendarNotiService.update(paramMap);
        if (update == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(update);
        return new ResponseEntity(successResponse , HttpStatus.OK);
    }
}
