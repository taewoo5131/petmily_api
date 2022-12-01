package com.petmily.api.repository;

import com.petmily.api.entity.Family;
import com.petmily.api.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;

    /**
     * Member 저장
     * @param member
     * @return
     */
    public String save(Member member) {
        em.persist(member);
        em.flush();
        return member.getId();
    }

    /**
     * PK로 Member에게 가족이 이미 존재하는지 여부
     * @return
     */
    public boolean existFamilyById(String memberIdx) {
        List<Family> resultList = em.createQuery("select m.family from Member m where m.idx =: idx", Family.class)
                .setParameter("idx", Long.parseLong(memberIdx))
                .getResultList();
        if (resultList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * PK로 Member find
     * @param requestPk
     * @return
     */
    public Member findMemberByIdx(String requestPk) {
        Member member = em.find(Member.class, Long.parseLong(requestPk));
        return member;
    }

    /**
     * ID로 Member count 반환
     * @param id
     * @return
     */
    public String existMemberByIdOrEmail(String id, String email) {
        Object existIdCnt = em.createQuery("select count(m) from Member m where m.id =:id or m.email =: email")
                .setParameter("id", id)
                .setParameter("email" , email)
                .getSingleResult();
        return existIdCnt.toString();
    }

    /**
     * ID로 Member find
     * @param id
     * @return
     */
    public Member findMemberById(String id) {
        Member findMember = em.createQuery("select m from Member m where m.id =:id", Member.class)
                .setParameter("id", id)
                .getSingleResult();
        return findMember;
    }

    /**
     * PK로 Member의 refreshToken 갱신
     * @param member
     * @param refreshToken
     */
    public void updateRefreshToken(Member member , String refreshToken) {
        Member findMember = em.find(Member.class, member.getIdx());
        findMember.setRefreshToken(refreshToken);
    }

    /**
     * 가족 추가
     * @param member
     * @param family
     */
    public void updateFamily(Member member, Family family) {
        Member findMember = em.find(Member.class, member.getIdx());
        findMember.setFamily(family);
    }
}
