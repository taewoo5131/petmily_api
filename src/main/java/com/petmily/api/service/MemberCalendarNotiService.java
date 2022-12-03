package com.petmily.api.service;

import com.petmily.api.entity.*;
import com.petmily.api.repository.FamilyRepository;
import com.petmily.api.repository.MemberCalendarNotiRepository;
import com.petmily.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCalendarNotiService {

    private final MemberRepository memberRepository;
    private final MemberCalendarNotiRepository memberCalendarNotiRepository;

    public List<MemberCalendarNoti> create(String memberIdx,Family paramFamily , CalendarNoti paramCalendarNoti) {
        List<MemberCalendarNoti> result = new ArrayList<>();
        // family에 해당하는 멤버 찾기
        List<Member> allMemberByFamily = memberRepository.findAllMemberByFamily(paramFamily);
        allMemberByFamily.forEach(member -> {
            log.info("찾은 멤버 >> {}"  , member);
            MemberCalendarNoti memberCalendarNoti = new MemberCalendarNoti();
            memberCalendarNoti.setMember(member);
            memberCalendarNoti.setCalendarNoti(paramCalendarNoti);
            memberCalendarNoti.setCheckYn(member.getIdx().toString().equals(memberIdx) ? AgreeEnum.Y : AgreeEnum.N);
            MemberCalendarNoti save = memberCalendarNotiRepository.save(memberCalendarNoti);
            result.add(save);
        });
        return result;
    }
}
