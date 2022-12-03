package com.petmily.api.dto;

import com.petmily.api.entity.AgreeEnum;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCalendarSelectDTO {
    private Long memberIdx;
    private String memberId;
    private String memberName;
    private Long familyIdx;
    private String familyName;
    private String familyRegDate;
    private Long calendarIdx;
    private String calendarTargetDate;
    private String calendarTargetName;
    private AgreeEnum calendarCheckYn;
}
