package com.project.kcs.response.userterms;

import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.response.terms.SearchTermsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.history.RevisionMetadata.RevisionType;


import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SearchUserTermsHistoryResponse {

    private Long key;
    private SearchTermsResponse terms;
    private LocalDateTime availableTime;
    private TermsAgreeType termsAgreeType;

    private Integer revisionNumber;
    private Instant revisionDate;
    private RevisionType revisionType;

}
