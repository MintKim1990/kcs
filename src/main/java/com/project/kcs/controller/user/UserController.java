package com.project.kcs.controller.user;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.entity.User;
import com.project.kcs.exception.ParameterException;
import com.project.kcs.request.user.InsertUserRequestForHptel;
import com.project.kcs.request.user.InsertUserRequestForKakao;
import com.project.kcs.request.user.LoginUserRequest;
import com.project.kcs.request.user.ModifyUserRequest;
import com.project.kcs.response.Response;
import com.project.kcs.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join/kakao")
    public Response joinForKakao(@RequestBody @Validated InsertUserRequestForKakao insertUserRequestForKakao, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        }

        return new Response(
                ResponseCode.SUCCESS,
                userService.join(User.setUser(insertUserRequestForKakao))
        );
    }

    @PostMapping("/join/hptel")
    public Object joinForHptel(@RequestBody @Validated InsertUserRequestForHptel insertUserRequestForHptel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        }

        return new Response(
                ResponseCode.SUCCESS,
                userService.join(User.setUser(insertUserRequestForHptel))
        );
    }

    @PutMapping("/modify/{key}")
    public Response modifyUsername(@RequestBody @Validated ModifyUserRequest modifyUserRequest,
                                   BindingResult bindingResult,
                                   @PathVariable Long key,
                                   HttpSession session) {

        if (bindingResult.hasErrors())
            throw new ParameterException(bindingResult);

        // 사용자 정보 수정
        User user = userService.modifyUserInfo(
                key,
                userService.getUserBySession(session),
                modifyUserRequest);

        // 수정된 정보로 세션에 유저정보 갱신
        LoginUserInfo loginUserInfo = userService.login(
                LoginUserRequest.setLoginUserRequest(user),
                session);

        return new Response(
                ResponseCode.SUCCESS,
                loginUserInfo
        );
    }

    @PostMapping("/login")
    public Response login(@RequestBody @Validated LoginUserRequest loginUserRequest,
                          BindingResult bindingResult,
                          HttpSession session
    ) {

        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        }

        return new Response(
                ResponseCode.SUCCESS,
                userService.login(loginUserRequest, session)
        );
    }

    @PostMapping("/logout")
    public Response logout(HttpSession session) {

        session.invalidate();

        return new Response(ResponseCode.SUCCESS);
    }

    @DeleteMapping("/delete/{key}")
    public Response delete(@PathVariable Long key, HttpSession session) {

        userService.delete(
                key,
                userService.getUserBySession(session));

        session.invalidate();

        return new Response(ResponseCode.SUCCESS);
    }


}
