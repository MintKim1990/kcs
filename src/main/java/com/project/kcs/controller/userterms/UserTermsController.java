package com.project.kcs.controller.userterms;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.entity.User;
import com.project.kcs.exception.ParameterException;
import com.project.kcs.request.userterms.InsertUserTermsRequest;
import com.project.kcs.response.Response;
import com.project.kcs.service.user.UserService;
import com.project.kcs.service.userterms.UserTermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/userterms")
@RestController
public class UserTermsController {

    private final UserService userService;
    private final UserTermsService userTermsService;

    @GetMapping("/search/all")
    public Response findAll(HttpSession session) {

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                userTermsService.findAll(user)
        );
    }

    @GetMapping("/search/terms")
    public Response findTerms(HttpSession session) {

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                userTermsService.findTermsAll(user)
        );
    }

    @GetMapping("/search/terms/history/{key}")
    public Response findTermsHistory(@PathVariable Long key) {
        return new Response(
                ResponseCode.SUCCESS,
                userTermsService.findAllHistory(key)
        );
    }

    @PostMapping("/registration")
    public Response registration(@RequestBody @Validated InsertUserTermsRequest insertUserTermsRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors())
            throw new ParameterException(bindingResult);

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                userTermsService.registration(insertUserTermsRequest, user)
        );
    }

}
