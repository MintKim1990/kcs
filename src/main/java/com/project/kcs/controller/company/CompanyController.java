package com.project.kcs.controller.company;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.entity.User;
import com.project.kcs.exception.ParameterException;
import com.project.kcs.request.company.InsertCompanyRequest;
import com.project.kcs.request.company.ModifyCompanyRequest;
import com.project.kcs.response.Response;
import com.project.kcs.service.company.CompanyService;
import com.project.kcs.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/company")
@RestController
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    @GetMapping("/search/{key}")
    public Response findOne(@PathVariable Long key, HttpSession session) {

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                companyService.findOne(key, user)
        );
    }

    @GetMapping("/search/all")
    public Response findAll(HttpSession session) {

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                companyService.findAll(user)
        );
    }

    @PostMapping("/registration")
    public Response registration(@RequestBody @Validated InsertCompanyRequest insertCompanyRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors())
            throw new ParameterException(bindingResult);

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                companyService.registration(insertCompanyRequest, user)
        );
    }

    @PutMapping("/modify/{key}")
    public Response modify(@RequestBody @Validated ModifyCompanyRequest modifyCompanyRequest,
                           BindingResult bindingResult,
                           @PathVariable Long key,
                           HttpSession session) {

        if (bindingResult.hasErrors())
            throw new ParameterException(bindingResult);

        User user = userService.getUserBySession(session);

        companyService.modify(modifyCompanyRequest, key, user);

        return new Response(ResponseCode.SUCCESS);
    }

    @DeleteMapping("/delete/{key}")
    public Response delete(@PathVariable Long key, HttpSession session) {

        User user = userService.getUserBySession(session);

        companyService.delete(key, user);

        return new Response(ResponseCode.SUCCESS);
    }


}
