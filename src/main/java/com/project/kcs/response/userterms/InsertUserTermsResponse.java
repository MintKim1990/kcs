package com.project.kcs.response.userterms;

import com.project.kcs.entity.UserTerms;
import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.entity.constant.TermsType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 약관동의 응답 VO
 */
@Data
@AllArgsConstructor
public class InsertUserTermsResponse {

    private Long key;
    private TermsType termsType;        
    private LocalDateTime availableTime;
    private TermsAgreeType termsAgreeType;

    public static InsertUserTermsResponse createRegistrationUserTermsResponse(UserTerms userTerms) {
        return new InsertUserTermsResponse(
                userTerms.getKey(),
                userTerms.getTerms().getType(),
                userTerms.getAvailableTime(),
                userTerms.getTermsAgreeType()
        );
    }
    
}
