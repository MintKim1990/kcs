package com.project.kcs.controller.terms;

import com.project.kcs.annotation.Auth;
import com.project.kcs.constant.ResponseCode;
import com.project.kcs.exception.ParameterException;
import com.project.kcs.request.terms.RegistrationTermsRequest;
import com.project.kcs.response.Response;
import com.project.kcs.service.terms.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/terms")
@Auth
@RestController
public class TermsController {

    private final TermsService termsService;

    @GetMapping("/search/all")
    public Response findAll() {
        return new Response(
                ResponseCode.SUCCESS,
                termsService.findAll()
        );
    }

    @PostMapping("/registration")
    public Response registration(@RequestBody @Validated RegistrationTermsRequest registrationTermsRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new ParameterException(bindingResult);

        return new Response(
                ResponseCode.SUCCESS,
                termsService.registration(registrationTermsRequest)
        );
    }

    @PutMapping("/modify/title/{key}")
    public Response modifyTitle(@PathVariable Long key, @RequestParam("title") @NotBlank String title) {

        termsService.modifyTitle(key, title);

        return new Response(ResponseCode.SUCCESS);
    }

    @PutMapping("/modify/content/{key}")
    public Response modifyContent(@PathVariable Long key, @RequestParam("content") @NotBlank String content) {

        termsService.modifyContent(key, content);

        return new Response(ResponseCode.SUCCESS);
    }

    @DeleteMapping("/delete/{key}")
    public Response delete(@PathVariable Long key) {

        termsService.delete(key);

        return new Response(ResponseCode.SUCCESS);
    }

}
