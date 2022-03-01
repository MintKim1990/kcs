package com.project.kcs.response.userterms;

import com.project.kcs.entity.UserTerms;
import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.response.terms.SearchTermsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 약관 조회 응답 VO
 */
@Data
@AllArgsConstructor
public class SearchUserTermsWithHistoryResponse {

    private SearchUserTermsResponse searchUserTermsResponse;
    private List<SearchUserTermsHistoryResponse> searchUserTermsHistoryResponseList;

}


