package com.project.kcs.request.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 사업장 등록 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCompanyRequest {

    @NotBlank(message = "사업자번호는 필수입니다.")
    @Pattern(regexp = "\\d{10}", message = "사업자등록번호는 -를 제외한 10자리를 입력해주세요.")
    private String businessNumber;

    @NotBlank(message = "개업일자는 필수입니다.")
    @Length(min = 8, max = 8, message = "개업일자는 년월일 8자리를 입력해주세요.")
    private String foundationDate;

    @NotBlank(message = "상호명은 필수입니다.")
    private String companyName;

    @Pattern(regexp = "\\d{13}", message = "법인등록번호는 -를 제외한 13자리를 입력해주세요.")
    private String corporationNumber;

    @Pattern(regexp = "\\d{9,11}", message = "사업장 전화번호는 -를 제외한 9~11자리 숫자만 입력해주세요.")
    private String companyTelephone;

}
