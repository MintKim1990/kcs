package com.project.kcs.response.scrapcredential;

import com.project.kcs.entity.ScrapCredential;
import com.project.kcs.entity.constant.CredentialType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertScrapCredentialResponse {

    private Long key;
    private CredentialType type;
    private String id;
    private String password;

    public static InsertScrapCredentialResponse createRegistrationScrapCredentialResponse(ScrapCredential scrapCredential) {
        return new InsertScrapCredentialResponse(
                scrapCredential.getKey(),
                scrapCredential.getType(),
                scrapCredential.getId(),
                scrapCredential.getPassword()
        );
    }

}
