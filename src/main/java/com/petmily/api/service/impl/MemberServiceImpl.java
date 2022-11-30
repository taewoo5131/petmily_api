package com.petmily.api.service.impl;

import com.petmily.api.common.ResponseEnum;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.dto.TokenDTO;
import com.petmily.api.entity.Member;
import com.petmily.api.repository.MemberRepository;
import com.petmily.api.security.JwtTokenProvider;
import com.petmily.api.security.SHA256;
import com.petmily.api.service.MemberService;
import com.petmily.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private SHA256 sha256 = new SHA256();

    private final MemberRepository memberRepository;

    private final TokenService tokenService;

    @Override
    public ResponseEntity Join(Map<String, Object> paramMap) {
        log.info("[serviceImpl join]");
        String[] checkKeyArr = {"id", "name", "password", "phoneNumber"};
        char checkType = 'j';
        char validCheck = this.validCheck(paramMap , checkKeyArr , checkType);
        if (validCheck == 'S') {
            Member member = new Member();
            member.setId(paramMap.get("id").toString());
            member.setPassword(sha256.joinEncrypt(paramMap.get("password").toString()));
            member.setName(paramMap.get("name").toString());
            member.setPhoneNumber(paramMap.get("phoneNumber").toString());
            member.setSalt(sha256.getSalt());
            String saveId = memberRepository.save(member);

            if (saveId.equals(member.getId())) {
                SuccessResponse successResponse = new SuccessResponse();
                return new ResponseEntity(successResponse,HttpStatus.OK);
            }
        } else if (validCheck == 'D') {
            return new ResponseEntity(ResponseEnum.DUP_MEMBER, HttpStatus.BAD_REQUEST);
        }
        throw new IllegalArgumentException("MemberServiceImpl.join 필수값 누락");
    }

    @Override
    public ResponseEntity login(Map<String, Object> paramMap) {
        log.info("[serviceImpl login]");
        String[] checkKeyArr = {"id" , "password"};
        char checkType = 'l';
        char validCheck = this.validCheck(paramMap, checkKeyArr , checkType);
        if (validCheck == 'S') {
            // id에 해당하는 Member
            Member findMember = memberRepository.findMemberById(paramMap.get("id").toString());
            // salt값 가져오기
            String findSalt = findMember.getSalt();
            // 가져온 salt값으로 cli가 넘긴 password 해싱처리
            String password = paramMap.get("password").toString();
            String encryptPassword = sha256.loginEncrypt(findSalt, password);

            if (findMember.getPassword().equals(encryptPassword)) {
                SuccessResponse successResponse = new SuccessResponse();
                String accessToken = tokenService.createAccessToken(findMember.getIdx().toString());
                String refreshToken = tokenService.createRefreshToken();
                TokenDTO tokenDTO = new TokenDTO(accessToken , refreshToken);

                // refresh token DB에 저장
                memberRepository.updateRefreshToken(findMember , refreshToken);

                successResponse.setData(tokenDTO);
                return new ResponseEntity(successResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity(ResponseEnum.LOGIN_FAIL, HttpStatus.BAD_REQUEST);
            }
        }
        throw new IllegalArgumentException("MemberServiceImpl.login 필수값 누락");
    }

    @Override
    public String getRefreshToken(String requestPk) {
        Member findMember = memberRepository.findMemberByIdx(requestPk);
        return findMember.getRefreshToken();
    }

    /**
     * [checkKeyArr] Null 체크
     * @param paramMap , keyArr , checkType
     * @return char
     *         ['S' : success , 'D' : duplicate ID , 'N' : NullPointerException]
     */
    private char validCheck(Map<String, Object> paramMap , String[] keyArr , char checkType) {
        try {
            for (String key : keyArr) {
                Optional.of(paramMap.get(key)).toString();
            }
            if (checkType == 'j') {
                String existId = memberRepository.existMemberById(paramMap.get("id").toString());
                if(Integer.parseInt(existId) > 0){
                    return 'D';
                }
            }
            return 'S';
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
