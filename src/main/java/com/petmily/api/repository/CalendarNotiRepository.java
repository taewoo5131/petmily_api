package com.petmily.api.repository;

import com.petmily.api.entity.CalendarNoti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CalendarNotiRepository {

    private final EntityManager em;

    public CalendarNoti findByCalendarIdx(Long calendarIdx) {
        CalendarNoti find = em.createQuery("select cm from CalendarNoti cm " +
                        "where cm.calendar.id =: calendarIdx", CalendarNoti.class)
                .setParameter("calendarIdx", calendarIdx)
                .getSingleResult();
        return find;
    }

    public CalendarNoti save(CalendarNoti calendarNoti) {
        em.persist(calendarNoti);
        return calendarNoti;
    }

    public Long delete(CalendarNoti calendarNoti) {
        em.remove(calendarNoti);
        return calendarNoti.getIdx();
    }
}
