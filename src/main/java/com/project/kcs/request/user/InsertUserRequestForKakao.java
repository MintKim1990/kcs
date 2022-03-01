package com.project.kcs.request.user;

import com.project.kcs.annotation.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 카카오 간편 회원가입 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserRequestForKakao {

    @NotBlank(message = "인증정보는 필수입니다.")
    private String identificationInfo;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @Length(min = 8, max = 8, message = "생년월일은 년월일 8자리를 입력해주세요.")
    @NotBlank(message = "생년월일은 필수입니다.")
    private String birthDate;

    @Gender
    @NotBlank(message = "성별은 필수입니다.")
    private String gender;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Pattern(regexp = "\\d{10,11}", message = "휴대폰번호는 -를 제외한 10~11자리 숫자만 입력해주세요.")
    @NotBlank(message = "휴대폰번호는 필수입니다.")
    private String hptel;

}
