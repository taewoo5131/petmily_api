package com.petmily.api.repository;

import com.petmily.api.entity.FamilyAgree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class FamilyAgreeRepository {

    private final EntityManager em;

    public FamilyAgree save(FamilyAgree familyAgree) {
        em.persist(familyAgree);
        em.flush();
        FamilyAgree findFamilyAgree = em.find(FamilyAgree.class, Long.parseLong(familyAgree.getIdx().toString()));
        return findFamilyAgree;
    }

    public FamilyAgree findByMemberIdx(String memberIdx) {
        FamilyAgree findFamilyAgree = em.createQuery("select f from FamilyAgree f where f.member.idx =: memberIdx" , FamilyAgree.class)
                .setParameter("memberIdx", Long.parseLong(memberIdx))
                .getSingleResult();
        return findFamilyAgree;
    }
}
