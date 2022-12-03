package com.petmily.api.repository;

import com.petmily.api.entity.MemberCalendarNoti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class MemberCalendarNotiRepository {

    private final EntityManager em;

    public MemberCalendarNoti save(MemberCalendarNoti memberCalendarNoti) {
        em.persist(memberCalendarNoti);
        em.flush();
        return em.find(MemberCalendarNoti.class , memberCalendarNoti.getId());
    }
}
