package com.project.kcs.response.terms;

import com.project.kcs.entity.constant.TermsStatus;
import com.project.kcs.entity.constant.TermsType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 약관 조회 응답 VO
 */
@Data
@AllArgsConstructor
public class SearchTermsResponse {

    private Long key;
    private TermsType type;
    private String title;
    private String content;
    private TermsStatus status;



}
