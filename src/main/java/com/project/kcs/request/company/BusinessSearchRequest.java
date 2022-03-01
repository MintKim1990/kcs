package com.project.kcs.request.company;

import com.project.kcs.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 사업장 진위확인 VO
 */
@Data
@NoArgsConstructor
public class BusinessSearchRequest {

    private String businessNumber;
    private String ownerName;
    private String foundationDate;

    @Builder
    public BusinessSearchRequest(String businessNumber, String ownerName, String foundationDate) {
        this.businessNumber = businessNumber;
        this.ownerName = ownerName;
        this.foundationDate = foundationDate;
    }

    public static BusinessSearchRequest setBusinessSearchRequestForSearch(InsertCompanyRequest insertCompanyRequest, User user) {
        return BusinessSearchRequest.builder()
                .businessNumber(insertCompanyRequest.getBusinessNumber())
                .ownerName(user.getName())
                .foundationDate(insertCompanyRequest.getFoundationDate())
                .build();
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("businessNumber", businessNumber);
        map.add("ownerName", ownerName);
        map.add("foundationDate", foundationDate);
        return map;
    }

}
