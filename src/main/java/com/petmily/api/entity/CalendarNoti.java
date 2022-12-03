package com.petmily.api.entity;

import javax.persistence.*;

@Entity
public class CalendarNoti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Long idx;

    @Column(name = "check_yn")
    private String checkYn;

    @OneToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}
