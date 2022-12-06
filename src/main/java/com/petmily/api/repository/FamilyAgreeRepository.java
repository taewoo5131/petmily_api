package com.petmily.api.repository;

import com.petmily.api.entity.FamilyAgree;
import com.petmily.api.entity.AgreeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class FamilyAgreeRepository {

    @PersistenceContext
    private EntityManager em;

    public FamilyAgree save(FamilyAgree familyAgree) {
        em.persist(familyAgree);
        em.flush();
        FamilyAgree findFamilyAgree = em.find(FamilyAgree.class, Long.parseLong(familyAgree.getIdx().toString()));
        return findFamilyAgree;
    }

    public FamilyAgree findByMemberIdx(String memberIdx) {
        try {
            String sql = "select f from FamilyAgree f " +
                    "join fetch f.member " +
                    "join fetch f.family " +
                    "where f.member.idx =: memberIdx";
            FamilyAgree findFamilyAgree = em.createQuery(sql, FamilyAgree.class)
                    .setParameter("memberIdx", Long.parseLong(memberIdx))
                    .getSingleResult();

            return findFamilyAgree;
        } catch (NoResultException e) {
            return null;
        }
    }

    public FamilyAgree updateAgreeYnByMemberIdxAndFamilyIdx(Map<String , Object> paramMap) {
        String sql = "select f from FamilyAgree f " +
                "join fetch f.member " +
                "join fetch f.family " +
                "where f.member.idx =: memberIdx and f.family.idx =: familyIdx";
        FamilyAgree findFamilyAgree = em.createQuery(sql, FamilyAgree.class)
                .setParameter("memberIdx", Long.parseLong(paramMap.get("memberIdx").toString()))
                .setParameter("familyIdx", Long.parseLong(paramMap.get("familyIdx").toString()))
                .getSingleResult();
        findFamilyAgree.setAgreeEnum(AgreeEnum.Y);
        return findFamilyAgree;
    }

    public FamilyAgree delete(Map<String , Object> paramMap) {
        FamilyAgree findFamilyAgree = this.findByMemberIdx(paramMap.get("memberIdx").toString());
        int deleteIdx = findFamilyAgree.getIdx().intValue();
        em.remove(findFamilyAgree);
        return findFamilyAgree;
    }
}
