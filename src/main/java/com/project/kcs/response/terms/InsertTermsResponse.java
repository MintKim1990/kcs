package com.project.kcs.response.terms;

import com.project.kcs.entity.Terms;
import com.project.kcs.entity.constant.TermsStatus;
import com.project.kcs.entity.constant.TermsType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 약관 등록 결과 VO
 */
@Data
@AllArgsConstructor
public class InsertTermsResponse {

    private Long key;
    private TermsType type;
    private String title;
    private String content;
    private TermsStatus status;

    public static InsertTermsResponse createRegistrationTermsResponse(Terms terms) {
        return new InsertTermsResponse(
                terms.getKey(),
                terms.getType(),
                terms.getTitle(),
                terms.getContent(),
                terms.getStatus()
        );
    }

}
