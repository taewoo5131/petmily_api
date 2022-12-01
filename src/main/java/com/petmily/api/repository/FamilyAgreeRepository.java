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

    public void save(FamilyAgree familyAgree) {
        em.persist(familyAgree);
    }
}
