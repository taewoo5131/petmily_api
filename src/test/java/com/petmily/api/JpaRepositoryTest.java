package com.petmily.api;

import com.petmily.api.entity.Member;
import com.petmily.api.repository.JpaRepositoryTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JpaRepositoryTest {

    @Autowired
    private JpaRepositoryTestRepository jpaRepositoryTestRepository;

    @Test
    void 테스트() {
        Optional<Member> findMember = jpaRepositoryTestRepository.findById(27L);
        findMember.ifPresent(member -> {
            System.out.println(member.getName());
        });

    }
}
