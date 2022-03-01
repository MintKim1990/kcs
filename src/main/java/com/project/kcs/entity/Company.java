package com.project.kcs.entity;

import com.project.kcs.entity.base.BaseEntity;
import com.project.kcs.request.company.ModifyCompanyRequest;
import com.project.kcs.request.company.InsertCompanyRequest;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "COMPANY", uniqueConstraints = {
        @UniqueConstraint(name = "COMPANY_USER_UNIQUE", columnNames = {"USER_KEY", "BUSINESS_NUMBER"}),
        @UniqueConstraint(name = "COMPANY_BUSINESS_NUMBER_UNIQUE", columnNames = "BUSINESS_NUMBER")
})
public class Company extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_KEY")
    private Long key;

    @Column(name = "BUSINESS_NUMBER", length = 10, nullable = false)
    private String businessNumber;

    @Column(name = "OWNER_NAME", length = 10, nullable = false)
    private String ownerName;

    @Column(name = "FOUNDATION_DATE", length = 8, nullable = false)
    private String foundationDate;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "CORPORATION_NUMBER", length = 13)
    private String corporationNumber;

    @Column(name = "COMPANY_TELEPHONE", length = 11)
    private String companyTelephone;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    private User user;

    @Builder
    public Company(String businessNumber, String ownerName, String foundationDate,
                   String companyName, String corporationNumber, String companyTelephone, User user) {
        this.businessNumber = businessNumber;
        this.ownerName = ownerName;
        this.foundationDate = foundationDate;
        this.companyName = companyName;
        this.corporationNumber = corporationNumber;
        this.companyTelephone = companyTelephone;
        this.user = user;
    }

    public static Company setCompany(InsertCompanyRequest insertCompanyRequest, User user) {

        Company company = Company.builder()
                .businessNumber(insertCompanyRequest.getBusinessNumber())
                .ownerName(user.getName())
                .foundationDate(insertCompanyRequest.getFoundationDate())
                .companyName(insertCompanyRequest.getCompanyName())
                .corporationNumber(insertCompanyRequest.getCorporationNumber())
                .companyTelephone(insertCompanyRequest.getCompanyTelephone())
                .build();

        company.setUser(user);

        return company;
    }

    /**
     * 사업장정보 수정
     * @param modifyCompanyRequest
     */
    public void ModifyCompanyInfo(ModifyCompanyRequest modifyCompanyRequest) {
        if (StringUtils.hasText(modifyCompanyRequest.getCompanyName())) { setCompanyName(modifyCompanyRequest.getCompanyName()); }
        if (StringUtils.hasText(modifyCompanyRequest.getCorporationNumber())) { setCorporationNumber(modifyCompanyRequest.getCorporationNumber()); }
        if (StringUtils.hasText(modifyCompanyRequest.getCompanyTelephone())) { setCompanyTelephone(modifyCompanyRequest.getCompanyTelephone()); }
    }

    /**
     * 양방향 편의메소드
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        user.getCompanyList().add(this);
    }
}
