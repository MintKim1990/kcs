package com.project.kcs.request.scrapcredential;

import com.project.kcs.entity.constant.CredentialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertScrapCredentialRequest {

    @NotNull(message = "인증기관 정보는 필수입니다.")
    private CredentialType type;

    @NotBlank(message = "인증기관 아이디는 필수입니다.")
    private String id;

    @NotBlank(message = "인증기관 패스워드는 필수입니다.")
    private String password;

}
