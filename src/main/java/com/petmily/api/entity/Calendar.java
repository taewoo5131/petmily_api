package com.petmily.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@Table(name = "CALENDAR")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "target_date")
    private String targetDate;

    @Column(name = "target_name")
    private String targetName;

}
