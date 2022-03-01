package com.project.kcs.config;

import com.project.kcs.interceptor.LoginInterceptor;
import com.project.kcs.interceptor.TermsAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final TermsAuthInterceptor termsAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 로그인여부 체크
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/modify/**")
                .addPathPatterns("/user/logout*")
                .addPathPatterns("/user/delete/**")
                .addPathPatterns("/company/**")
                .addPathPatterns("/terms/**")
                .addPathPatterns("/userterms/**")
                .addPathPatterns("/scrapcredential/**");

        // 약관권한 체크
        registry.addInterceptor(termsAuthInterceptor)
                .addPathPatterns("/**");

    }

}

