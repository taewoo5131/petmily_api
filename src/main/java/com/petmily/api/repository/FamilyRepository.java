package com.petmily.api.repository;

import com.petmily.api.entity.Family;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class FamilyRepository {

    private final EntityManager em;


    public Family save(Family family) {
        em.persist(family);
        em.flush();
        Family saveFamily = em.find(Family.class, Long.parseLong(family.getIdx().toString()));
        return saveFamily;
    }
}
