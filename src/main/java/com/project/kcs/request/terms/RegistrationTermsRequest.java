package com.project.kcs.request.terms;

import com.project.kcs.entity.constant.TermsStatus;
import com.project.kcs.entity.constant.TermsType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationTermsRequest {

    @NotNull(message = "약관타입은 필수입니다.")
    private TermsType type;

    @NotBlank(message = "약관타이틀은 필수입니다.")
    private String title;

    private String content;

}
