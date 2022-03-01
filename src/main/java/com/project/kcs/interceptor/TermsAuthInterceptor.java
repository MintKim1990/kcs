package com.project.kcs.interceptor;

import com.project.kcs.annotation.Auth;
import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 약관등록 및 수정은 인증된 사용자만 가능
 */
@Slf4j
@Component
public class TermsAuthInterceptor implements HandlerInterceptor {

    private final String USER_SESSION = "UserInfo";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth annotation = handlerMethod.getBeanType().getAnnotation(Auth.class);

        if (Objects.nonNull(annotation)) {

            HttpSession session = request.getSession();

            LoginUserInfo loginUserInfo = (LoginUserInfo) session.getAttribute(USER_SESSION);

            // 약관관련 데이터는 김민태라는 이름으로 가입된 회원만 등록, 수정가능
            if (!loginUserInfo.getName().equals("김민태")) {
                throw new AuthException("해당 페이지에 접근권한이 없습니다.");
            }
        }

        return true;
    }
}
