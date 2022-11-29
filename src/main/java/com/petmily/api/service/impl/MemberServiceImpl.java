package com.petmily.api.service.impl;

import com.petmily.api.common.ResponseEnum;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.entity.Member;
import com.petmily.api.repository.MemberRepository;
import com.petmily.api.security.SHA256;
import com.petmily.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private SHA256 sha256 = new SHA256();

    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity Join(Map<String, Object> paramMap) {
        log.info("serviceImpl join");
        char validCheck = this.joinValidCheck(paramMap);
        if (validCheck == 'S') {
            Member member = new Member();
            try {
                member.setId(paramMap.get("id").toString());
                member.setPassword(sha256.joinEncrypt(paramMap.get("password").toString()));
                member.setName(paramMap.get("name").toString());
                member.setPhoneNumber(paramMap.get("phoneNumber").toString());
                member.setSalt(sha256.getSalt());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String saveId = memberRepository.save(member);

            if (saveId.equals(member.getId())) {
                SuccessResponse successResponse = new SuccessResponse();
                HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("test", "taewoo");
                successResponse.setData(objectObjectHashMap);
                return new ResponseEntity(successResponse,HttpStatus.OK);
            }
        } else if (validCheck == 'D') {
            return new ResponseEntity(ResponseEnum.DUP_MEMBER, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(ResponseEnum.ILLEGAL_ARGS_ERROR , HttpStatus.BAD_REQUEST);
    }

    /**
     * [id , name , password , phoneNumber] Null 체크
     * @param paramMap
     * @return char
     *         ['S' : success , 'D' : duplicate ID , 'N' : NullPointerException]
     */
    private char joinValidCheck(Map<String, Object> paramMap) {
        try {
            String id = Optional.of(paramMap.get("id")).toString();
            String name = Optional.of(paramMap.get("name")).toString();
            String password = Optional.of(paramMap.get("password")).toString();
            String phoneNumber = Optional.of(paramMap.get("phoneNumber")).toString();

            String existId = memberRepository.findMemberById(paramMap.get("id").toString());
            if(Integer.parseInt(existId) > 0){
                return 'D';
            }
            return 'S';
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 'N';
        }
    }
}
