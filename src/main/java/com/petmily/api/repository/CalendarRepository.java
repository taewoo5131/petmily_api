package com.petmily.api.repository;

import com.petmily.api.entity.Calendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CalendarRepository {
    private final EntityManager em;

    public Calendar save(Calendar calendar) {
        em.persist(calendar);
        em.flush();
        return em.find(Calendar.class , calendar.getId());
    }
}
