package com.petmily.api.repository;

import com.petmily.api.entity.Calendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CalendarRepository {

    @PersistenceContext
    private EntityManager em;

    public Calendar save(Calendar calendar) {
        em.persist(calendar);
        em.flush();
        return em.find(Calendar.class , calendar.getId());
    }

    public Long update(Map<String, Object> paramMap) {
        Calendar findCalendar = em.find(Calendar.class, Long.parseLong(paramMap.get("calendarIdx").toString()));
        findCalendar.setTargetName(paramMap.get("targetName").toString());
        findCalendar.setTargetDate(paramMap.get("targetDate").toString());
        return findCalendar.getId();
    }

    public Long delete(Map<String, Object> paramMap) {
        Calendar findCalendar = em.createQuery("select c from Calendar c join fetch c.family f where f.idx =: familyIdx and c.id =: calendarIdx", Calendar.class)
                .setParameter("familyIdx", Long.parseLong(paramMap.get("familyIdx").toString()))
                .setParameter("calendarIdx", Long.parseLong(paramMap.get("calendarIdx").toString()))
                .getSingleResult();
        em.remove(findCalendar);
        return Long.parseLong(paramMap.get("calendarIdx").toString());
    }
}
