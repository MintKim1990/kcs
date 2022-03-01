package com.project.kcs.service.company;

import com.project.kcs.request.company.BusinessSearchRequest;
import com.project.kcs.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class AllcreditClient {

    @Value("${business.allcredit.url}")
    private String allcreditUrl;

    public Response search(BusinessSearchRequest businessSearchRequest) {

        URI uri = UriComponentsBuilder
                .fromUriString(allcreditUrl)
                .queryParams(businessSearchRequest.toMultiValueMap())
                .port(8080)
                .build()
                .encode()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .build();

        ResponseEntity<Response> responseEntity = new RestTemplate()
                .exchange(
                        uri,
                        HttpMethod.GET,
                        requestEntity,
                        new ParameterizedTypeReference<Response>() {
                        }
                );

        return responseEntity.getBody();
    }

}
