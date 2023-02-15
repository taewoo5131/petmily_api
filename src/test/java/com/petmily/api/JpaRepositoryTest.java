package com.petmily.api;

import com.petmily.api.entity.Member;
import com.petmily.api.repository.JpaRepositoryTestRepository;
import com.petmily.api.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JpaRepositoryTest {

    @Autowired
    private JpaRepositoryTestRepository jpaRepositoryTestRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void 테스트() {
        Optional<Member> findMember = jpaRepositoryTestRepository.findById(27L);
        findMember.ifPresent(member -> {
            System.out.println(member.getName());
        });
    }

    @Test
    @Transactional
    void N더하기1테스트() {
//        em.find(Member.class, 27L);
    }

    @Test
    @Transactional
    void N더하기1발생() {
        Member findMember = memberRepository.findMemberById("test");
        findMember.getFamily().getFamilyName();
    }

    @Test
    @Transactional
    void EntityManager가_제공하는_기능() {
        Member member = em.find(Member.class, 27L);

    }

    @Test
    @Transactional
    void fetchjoin() {
        em.createQuery("select m from Member m join fetch Family f");
    }
}
