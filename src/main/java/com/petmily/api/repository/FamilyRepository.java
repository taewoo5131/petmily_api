package com.petmily.api.repository;

import com.petmily.api.entity.Family;
import com.petmily.api.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FamilyRepository {

    private final EntityManager em;

    public Family save(Family family) {
        em.persist(family);
        em.flush();
        Family saveFamily = em.find(Family.class, Long.parseLong(family.getIdx().toString()));
        return saveFamily;
    }

    public Family findFamilyByIdx(String familyIdx) {
        Family family = em.find(Family.class, Long.parseLong(familyIdx));
        return family;
    }
}
