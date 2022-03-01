package com.project.kcs.request.userterms;

import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.entity.constant.TermsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 사용자 약관 동의 요청 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserTermsRequest {

    @NotNull(message = "동의할 약관 타입정보는 필수입니다.")
    private TermsType termsType;

    @NotNull(message = "약관 동의여부는 필수입니다.")
    private TermsAgreeType termsAgreeType;

}
