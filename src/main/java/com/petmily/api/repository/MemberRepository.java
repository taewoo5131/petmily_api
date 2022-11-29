package com.petmily.api.repository;

import com.petmily.api.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

    public String findMemberById(String id) {
        Object existIdCnt = em.createQuery("select count(m) from Member m where m.id =:id")
                .setParameter("id", id)
                .getSingleResult();
        return existIdCnt.toString();
    }

}
