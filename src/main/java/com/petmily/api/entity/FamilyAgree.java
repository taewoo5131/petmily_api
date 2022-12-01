package com.petmily.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAMILY_AGREE")
public class FamilyAgree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_agree_id")
    private Long idx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @Enumerated(EnumType.STRING)
    @Column(name = "agree_yn")
    private FamilyAgreeEnum familyAgreeEnum;

    public FamilyAgree(Member member, Family family, FamilyAgreeEnum familyAgreeEnum) {
        this.member = member;
        this.family = family;
        this.familyAgreeEnum = familyAgreeEnum;
    }
}
