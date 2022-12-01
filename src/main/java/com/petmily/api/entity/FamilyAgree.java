package com.petmily.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@Table(name = "FAMILY_AGREE")
public class FamilyAgree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_agree_id")
    private Long idx;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "agree_yn")
    private int agreeYn;
}
