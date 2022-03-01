package com.project.kcs.request.user;

import com.project.kcs.annotation.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 사용자 정보 변경 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserRequest {

    private String userName;

    @Length(min = 8, max = 8, message = "생년월일은 년월일 8자리를 입력해주세요.")
    private String birthDate;

    @Gender
    private String gender;

    private String name;

    @Pattern(regexp = "\\d{10,11}", message = "휴대폰번호는 -를 제외한 10~11자리 숫자만 입력해주세요.")
    private String hptel;

}
