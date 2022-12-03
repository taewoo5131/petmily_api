package com.petmily.api.repository;

import com.petmily.api.entity.CalendarNoti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CalendarNotiRepository {

    private final EntityManager em;

    public CalendarNoti save(CalendarNoti calendarNoti) {
        em.persist(calendarNoti);
        return calendarNoti;
    }
}
