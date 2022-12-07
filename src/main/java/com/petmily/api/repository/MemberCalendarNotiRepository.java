package com.petmily.api.repository;

import com.petmily.api.dto.MemberCalendarSelectDTO;
import com.petmily.api.entity.AgreeEnum;
import com.petmily.api.entity.MemberCalendarNoti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
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
        String sql = "select new com.petmily.api.dto.MemberCalendarSelectDTO(mcn.member.idx ," +
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

    public MemberCalendarNoti update(Map<String, Object> paramMap) {
        String memberIdx = paramMap.get("memberIdx").toString();
        String calendarIdx = paramMap.get("calendarIdx").toString();
        String checkYn = paramMap.get("checkYn").toString();

        MemberCalendarNoti memberCalendarNoti = em.createQuery("select mcn from MemberCalendarNoti mcn " +
                        "left join fetch mcn.calendarNoti cn " +
                        "left join fetch cn.calendar c " +
                        "left join fetch c.family f " +
                        "left join fetch mcn.member m " +
                        "where c.id =: calendarIdx " +
                        "and m.idx =: memberIdx", MemberCalendarNoti.class)
                .setParameter("calendarIdx", Long.parseLong(calendarIdx))
                .setParameter("memberIdx", Long.parseLong(memberIdx))
                .getSingleResult();
        log.info("update query >> {} " , memberCalendarNoti);
        memberCalendarNoti.setCheckYn(checkYn.equals("Y") ? AgreeEnum.Y : AgreeEnum.N);
        em.flush();
        return memberCalendarNoti;
    }

    public List<MemberCalendarNoti> findAllByNotiIdx(Long notiIdx) {
        List<MemberCalendarNoti> findAll = em.createQuery("select mcn from MemberCalendarNoti mcn " +
                        "left join mcn.member m " +
                        "left join m.family f " +
                        "left join mcn.calendarNoti cn " +
                        "left join cn.calendar c " +
                        "where cn.idx =: notiIdx", MemberCalendarNoti.class)
                .setParameter("notiIdx", notiIdx)
                .getResultList();
        return findAll;
    }

    public int deleteByNotiIdx(Long notiIdx) {
        log.info("deleteByNotiIdx >> {} " , notiIdx);
        Query query = em.createQuery("delete from MemberCalendarNoti mcn " +
                        "where mcn.calendarNoti.idx =: notiIdx")
                .setParameter("notiIdx", notiIdx);
        int rows = query.executeUpdate();
        return rows;
    }
}
