package com.project.kcs.interceptor;

import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final String USER_SESSION = "UserInfo";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();

        LoginUserInfo loginUserInfo = (LoginUserInfo) session.getAttribute(USER_SESSION);

        if (Objects.isNull(loginUserInfo))
            throw new LoginException("로그인이 필요합니다.");

        return true;
    }
}
