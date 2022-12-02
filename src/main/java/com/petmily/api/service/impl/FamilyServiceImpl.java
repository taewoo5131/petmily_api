package com.petmily.api.service.impl;

import com.petmily.api.common.PetmilyUtil;
import com.petmily.api.common.ResponseEnum;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.entity.Family;
import com.petmily.api.entity.FamilyAgree;
import com.petmily.api.entity.FamilyAgreeEnum;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        String[] checkKeyArr = {"memberIdx", "familyName"};
        if (!PetmilyUtil.parameterNullCheck(paramMap , checkKeyArr)) {
            throw new IllegalArgumentException("FamilyServiceImpl.create 필수값 누락");
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
            familyAgree.setFamilyAgreeEnum(FamilyAgreeEnum.Y);
            familyAgreeRepository.save(familyAgree);

            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setData(saveFamily);
            return new ResponseEntity(successResponse, HttpStatus.OK);
        }
        return null;
    }

    @Override
    public ResponseEntity regist(List<Map<String, Object>> paramList) {
        log.info("[FamilyServiceImpl regist]");
        String[] checkKeyArr = {"memberIdx" , "familyIdx"};
        List<FamilyAgree> resultList = new ArrayList();
        for (Map<String, Object> paramMap : paramList) {
            if (PetmilyUtil.parameterNullCheck(paramMap, checkKeyArr)) {
                FamilyAgree existMember = familyAgreeRepository.findByMemberIdx(paramMap.get("memberIdx").toString());
                if (existMember != null) {
                    return new ResponseEntity(ResponseEnum.EXIST_FAMILY, HttpStatus.BAD_REQUEST);
                }

                Member findMember = memberRepository.findMemberByIdx(paramMap.get("memberIdx").toString());
                Family findFamily = familyRepository.findFamilyByIdx(paramMap.get("familyIdx").toString());
                FamilyAgree familyAgree = new FamilyAgree(findMember, findFamily, FamilyAgreeEnum.N);
                FamilyAgree save = familyAgreeRepository.save(familyAgree);
                if (save != null) {
                    resultList.add(save);
                }
            }
        }
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(resultList);
        return new ResponseEntity(successResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity response(Map<String, Object> paramMap) {
        log.info("[FamilyServiceImpl response]");
        String[] checkKeyArr = {"memberIdx", "familyIdx", "agree_yn"};
        if (!PetmilyUtil.parameterNullCheck(paramMap, checkKeyArr)) {
            throw new IllegalArgumentException("FamilyServiceImpl.response 필수값 누락");
        }

        String agree_yn = paramMap.get("agree_yn").toString();
        if (FamilyAgreeEnum.Y.toString().equals(agree_yn)) {
            FamilyAgree familyAgree = familyAgreeRepository.updateAgreeYnByMemberIdxAndFamilyIdx(paramMap);
            if (familyAgree != null) {
                memberRepository.updateFamily(familyAgree.getMember() , familyAgree.getFamily());
                SuccessResponse successResponse = new SuccessResponse();
                successResponse.setData(familyAgree);
                return new ResponseEntity(successResponse, HttpStatus.OK);
            }
        } else if (FamilyAgreeEnum.N.toString().equals(agree_yn)) {
            FamilyAgree delete = familyAgreeRepository.delete(paramMap);
            if (delete != null ) {
                SuccessResponse successResponse = new SuccessResponse();
                successResponse.setData(delete);
                return new ResponseEntity(successResponse, HttpStatus.OK);
            }
        } else {
            throw new IllegalArgumentException("FamilyServiceImpl.response agree_yn Enum값에 해당 안됨");
        }

        return null;
    }
}
