package com.project.kcs.controller.scrapcredential;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.entity.User;
import com.project.kcs.exception.ParameterException;
import com.project.kcs.request.scrapcredential.InsertScrapCredentialRequest;
import com.project.kcs.response.Response;
import com.project.kcs.service.scrapcredential.ScrapCredentialService;
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
@RequestMapping("/scrapcredential")
@RestController
public class ScrapCredentialController {

    private final UserService userService;
    private final ScrapCredentialService scrapCredentialService;

    @GetMapping("/search/all")
    public Response findAll(HttpSession session) {

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                scrapCredentialService.findAll(user)
        );
    }

    @PostMapping("/registration")
    public Response registration(@RequestBody @Validated InsertScrapCredentialRequest insertScrapCredentialRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors())
            throw new ParameterException(bindingResult);

        User user = userService.getUserBySession(session);

        return new Response(
                ResponseCode.SUCCESS,
                scrapCredentialService.registration(insertScrapCredentialRequest, user)
        );
    }

    @PutMapping("/modify/{key}")
    public Response modify(@PathVariable Long key,
                           @RequestParam @NotBlank String id,
                           @RequestParam @NotBlank String password,
                           HttpSession session) {

        User user = userService.getUserBySession(session);

        scrapCredentialService.modifyCredentialInfo(key, id, password, user);

        return new Response(ResponseCode.SUCCESS);
    }

    @DeleteMapping("/delete/{key}")
    public Response delete(@PathVariable Long key, HttpSession session) {

        User user = userService.getUserBySession(session);

        scrapCredentialService.delete(key, user);

        return new Response(ResponseCode.SUCCESS);
    }

}
