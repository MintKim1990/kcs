package com.project.kcs.config;

import com.project.kcs.dto.user.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

/**
 * ThreadLocal 방식에 스프링시큐리티 방식으로 구현 예정 (현재는 스프링에서 프록시 세션객체만 넘겨줘서 세션정보를 읽을수 없음)
 */
@RequiredArgsConstructor
@Component
public class UserAuditorAware implements AuditorAware<Long> {

    private final HttpSession session;

    @Override
    public Optional<Long> getCurrentAuditor() {

        if (Objects.nonNull(session)) {

            LoginUserInfo loginUserInfo = (LoginUserInfo) session.getAttribute("USER_SESSION");

            if (Objects.nonNull(loginUserInfo))
                return Optional.ofNullable(loginUserInfo.getKey());

        }

        return null;
    }
}
