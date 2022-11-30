package com.petmily.api.repository;

import com.petmily.api.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;

    public String save(Member member) {
        em.persist(member);
        em.flush();
        return member.getId();
    }

    public String existMemberById(String id) {
        Object existIdCnt = em.createQuery("select count(m) from Member m where m.id =:id")
                .setParameter("id", id)
                .getSingleResult();
        return existIdCnt.toString();
    }

    public Member findMemberById(String id) {
        Member findMember = em.createQuery("select m from Member m where m.id =:id", Member.class)
                .setParameter("id", id)
                .getSingleResult();
        return findMember;
    }

    public void updateRefreshToken(Member member , String refreshToken) {
        Member findMember = em.find(Member.class, member.getIdx());
        findMember.setRefreshToken(refreshToken);
    }
}
