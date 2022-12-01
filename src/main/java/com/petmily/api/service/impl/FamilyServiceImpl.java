package com.petmily.api.service.impl;

import com.petmily.api.common.ResponseEnum;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.entity.Family;
import com.petmily.api.entity.FamilyAgree;
import com.petmily.api.entity.Member;
import com.petmily.api.repository.FamilyAgreeRepository;
import com.petmily.api.repository.FamilyRepository;
import com.petmily.api.repository.MemberRepository;
import com.petmily.api.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService {

    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final FamilyAgreeRepository familyAgreeRepository;

    @Override
    @Transactional
    public ResponseEntity create(Map<String, Object> paramMap) {
        log.info("[FamilyServiceImpl create]");
        if (paramMap.get("memberIdx") == null || paramMap.get("memberIdx").equals("")) {
            return new ResponseEntity(ResponseEnum.ILLEGAL_ARGS_ERROR , HttpStatus.BAD_REQUEST);
        }
        String memberIdx = paramMap.get("memberIdx").toString();
        boolean existFamily = memberRepository.existFamilyById(memberIdx);
        if (existFamily) {
            return new ResponseEntity(ResponseEnum.EXIST_FAMILY, HttpStatus.BAD_REQUEST);
        }

        Family family = new Family();
        family.setFamilyName(paramMap.get("familyName").toString());
        family.setReg_date(LocalDate.now().toString());
        Family saveFamily = familyRepository.save(family);
        if (saveFamily != null) {
            Member findMember = memberRepository.findMemberByIdx(memberIdx);
            memberRepository.updateFamily(findMember , family);

            FamilyAgree familyAgree = new FamilyAgree();
            familyAgree.setMember(findMember);
            familyAgree.setFamily(family);
            familyAgree.setAgreeYn(1);
            familyAgreeRepository.save(familyAgree);

            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setData(saveFamily);
            return new ResponseEntity(successResponse, HttpStatus.OK);
        }
        return null;
    }
}
