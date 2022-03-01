package com.project.kcs.response.company;

import com.project.kcs.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 사업장 등록 응답 VO
 */
@Data
@AllArgsConstructor
public class InsertCompanyResponse {

    private Long key;
    private String businessNumber;
    private String ownerName;
    private String foundationDate;
    private String companyName;
    private String corporationNumber;
    private String companyTelephone;

    public static InsertCompanyResponse createRegistrationCompanyResponse(Company company) {
        return new InsertCompanyResponse(
                company.getKey(),
                company.getBusinessNumber(),
                company.getOwnerName(),
                company.getFoundationDate(),
                company.getCompanyName(),
                company.getCorporationNumber(),
                company.getCompanyTelephone()
        );
    }

}
