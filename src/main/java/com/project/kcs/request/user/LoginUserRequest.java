package com.project.kcs.request.user;

import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 로그인 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {

    @NotNull(message = "인증정보는 필수입니다.")
    private CertificationType certificationType;

    @NotBlank(message = "인증정보는 필수입니다.")
    private String identificationInfo;

    public static LoginUserRequest setLoginUserRequest(User user) {
        return new LoginUserRequest(
                user.getCertificationType(),
                user.getIdentificationInfo()
        );
    }

}
