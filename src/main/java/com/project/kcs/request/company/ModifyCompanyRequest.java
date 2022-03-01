package com.project.kcs.request.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 사업장 정보 수정 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCompanyRequest {

    private String companyName;

    @Pattern(regexp = "\\d{13}", message = "법인등록번호는 -를 제외한 13자리를 입력해주세요.")
    private String corporationNumber;

    @Pattern(regexp = "\\d{9,11}", message = "사업장 전화번호는 -를 제외한 9~11자리 숫자만 입력해주세요.")
    private String companyTelephone;

}
