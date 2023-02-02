package com.petmily.api.repository;

import com.petmily.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRepositoryTestRepository extends JpaRepository<Member , Long> {
    Optional<Member> findById(Long id);
}
