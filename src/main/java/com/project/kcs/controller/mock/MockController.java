package com.project.kcs.controller.mock;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

/**
 * 사업장진위확인을 위한 MockController
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/business/search")
@RestController
public class MockController {

    private final List<String> SUCCESS_BUSINESSNUMBER = Arrays.asList("7348800358", "7348800359");
    private final List<String> SUCCESS_OWNERNAME = Arrays.asList("테스트", "대표자", "김민태");
    private final List<String> SUCCESS_FOUNDATIONDATE = Arrays.asList("20221231", "20220224");

    @GetMapping("/allcredit")
    public Response allcredit(
            @RequestParam @NotBlank String businessNumber,
            @RequestParam @NotBlank String ownerName,
            @RequestParam @NotBlank String foundationDate
    ) {
        return search(businessNumber, ownerName, foundationDate);
    }

    @GetMapping("/crefia")
    public Response crefia(
            @RequestParam @NotBlank String businessNumber,
            @RequestParam @NotBlank String ownerName,
            @RequestParam @NotBlank String foundationDate
    ) {
        return search(businessNumber, ownerName, foundationDate);
    }

    @GetMapping("/hometax")
    public Response hometax(
            @RequestParam @NotBlank String businessNumber,
            @RequestParam @NotBlank String ownerName,
            @RequestParam @NotBlank String foundationDate
    ) {
        return search(businessNumber, ownerName, foundationDate);
    }

    private Response search(String businessNumber, String ownerName, String foundationDate) {

        if (validBybusinessNumber(businessNumber) && validByownerName(ownerName) && validByfoundationDate(foundationDate))
            return new Response(ResponseCode.SUCCESS);

        return new Response(ResponseCode.FAIL);
    }

    private boolean validBybusinessNumber(String businessNumber) {
        return SUCCESS_BUSINESSNUMBER.stream().anyMatch(str -> str.equals(businessNumber));
    }

    private boolean validByownerName(String ownerName) {
        return SUCCESS_OWNERNAME.stream().anyMatch(str -> str.equals(ownerName));
    }

    private boolean validByfoundationDate(String foundationDate) {
        return SUCCESS_FOUNDATIONDATE.stream().anyMatch(str -> str.equals(foundationDate));
    }

}
