package com.project.kcs.response.scrapcredential;

import com.project.kcs.entity.constant.CredentialType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchScrapCredentialResponse {

    private Long key;
    private CredentialType type;
    private String id;
    private String password;

}
