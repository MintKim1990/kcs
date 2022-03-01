package com.project.kcs.response.userterms;

import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.response.terms.SearchTermsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 사용자 약관 조회 응답 VO
 */
@Data
@AllArgsConstructor
public class SearchUserTermsResponse {

    private Long key;
    private SearchTermsResponse terms;
    private LocalDateTime availableTime;
    private TermsAgreeType termsAgreeType;

}


