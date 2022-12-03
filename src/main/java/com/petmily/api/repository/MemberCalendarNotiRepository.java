package com.petmily.api.repository;

import com.petmily.api.dto.MemberCalendarSelectDTO;
import com.petmily.api.entity.MemberCalendarNoti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public List<MemberCalendarSelectDTO> findByMemberIdxAndSearchDateBetween(Map<String, Object> paramMap) {
        StringBuilder dateBetween = new StringBuilder();
        String ymdFormat = "\'%Y%m%d\'";
        if (!paramMap.get("searchStartDate").toString().equals("0")) {
            dateBetween.append(" and c.targetDate >= STR_TO_DATE(" + paramMap.get("searchStartDate").toString() + "," + ymdFormat + ")");
        }
        if (!paramMap.get("searchEndDate").toString().equals("0")) {
            dateBetween.append(" and c.targetDate <= STR_TO_DATE(" + paramMap.get("searchEndDate").toString() + "," + ymdFormat + ")");
        }
        log.info("query ?? {} " , dateBetween.toString());
        String sql = "select new com.petmily.api.dto.MemberCalendarSelectDTO(mcn.id ," +
                "mcn.member.id , mcn.member.name , c.family.idx , c.family.familyName, " +
                "c.family.reg_date, c.id , c.targetDate , c.targetName, mcn.checkYn)" +
                " from MemberCalendarNoti mcn " +
                "left join mcn.calendarNoti as cn " +
                "left join cn.calendar as c " +
                "where mcn.member.idx =: memberIdx " +
                dateBetween.toString();
        List resultList = em.createQuery(sql, MemberCalendarSelectDTO.class)
                .setParameter("memberIdx", Long.parseLong(paramMap.get("memberIdx").toString()))
                .getResultList();
        resultList.forEach(item -> {
            log.info("[MemberCalendarNotiRepository findByMemberIdxAndSearchDateBetween 조회 결과] >> {} " , item);
        });
        return resultList;
    }
}
