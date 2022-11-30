package com.petmily.api.controller;

import com.petmily.api.common.ResponseEnum;
import com.petmily.api.common.SuccessResponse;
import com.petmily.api.service.MemberService;
import com.petmily.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final TokenService tokenService;

    @GetMapping("/join")
    public void getJoin() {
        log.info("[MemberController.getJoin] >> {} " , "join");
    }

    @PostMapping("/join")
    public ResponseEntity postJoin(
            @RequestBody Map<String , Object> paramMap
        ) {
        log.info("[MemberController.postJoin] >> {} " , "join");
        return memberService.Join(paramMap);
    }

    @GetMapping("/login")
    public void getLogin() {
        log.info("[MemberController.getLogin] >> {} " , "login");
    }

    @PostMapping("/login")
    public ResponseEntity postLogin(
            @RequestBody Map<String , Object> paramMap
    ) {
        log.info("[MemberController.postLogin] >> {} " , "login");
        return memberService.login(paramMap);
    }

    @GetMapping("/logout")
    public ResponseEntity getLogout(
            @RequestParam("memberId") String memberId
    ) {
        log.info("[MemberController.getLogout] >> {} " , "logout");
        return memberService.logout(memberId);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity refreshToken(
            @RequestParam("pk") String requestPk
    ) {
        log.info("[MemberController.refreshToken] >> {} " , requestPk);
        String refreshToken = memberService.getRefreshToken(requestPk);
        if (refreshToken == null) {
            return new ResponseEntity(ResponseEnum.NO_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
        }
        boolean refreshTokenCheck = tokenService.tokenCheck(refreshToken);
        if(refreshTokenCheck){
            // accessToken 재발급
            String accessToken = tokenService.createAccessToken(requestPk);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMsg(ResponseEnum.REISSUANCE_ACCESS_TOKEN.getMsg());
            successResponse.setStatus(ResponseEnum.REISSUANCE_ACCESS_TOKEN.getCode());
            successResponse.setData(Map.of("accessToken" , accessToken));
            return new ResponseEntity(successResponse, HttpStatus.OK);
        } else {
            // 로그아웃 후 재로그인
            return memberService.logout(requestPk);
        }
    }
}
