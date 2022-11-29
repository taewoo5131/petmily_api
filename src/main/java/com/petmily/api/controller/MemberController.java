package com.petmily.api.controller;

import com.petmily.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public void getJoin() {
        log.info("MemberController.getJoin >> {} " , "join");
    }

    @PostMapping("/join")
    public ResponseEntity postJoin(
            @RequestBody Map<String , Object> paramMap
        ) {
        log.info("MemberController.postJoin >> {} " , "join");
        return memberService.Join(paramMap);
    }

    @GetMapping("/login")
    public void getLogin() {
        log.info("MemberController.getLogin >> {} " , "login");
    }

    @PostMapping("/login")
    public void postLogin() {
        log.info("MemberController.postLogin >> {} " , "login");
    }
}
