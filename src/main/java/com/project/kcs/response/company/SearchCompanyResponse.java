package com.project.kcs.response.company;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 사업장 조회 응답 VO
 */
@Data
@AllArgsConstructor
public class SearchCompanyResponse {

    private Long key;
    private String businessNumber;
    private String ownerName;
    private String foundationDate;
    private String companyName;
    private String corporationNumber;
    private String companyTelephone;

}
