package com.project.kcs.exception.advice;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.exception.AuthException;
import com.project.kcs.exception.LoginException;
import com.project.kcs.exception.ParameterException;
import com.project.kcs.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParameterException.class)
    public Response parameterExceptionHandler(ParameterException Error) {
        return new Response(
                ResponseCode.FAIL,
                Optional.ofNullable(
                    Error.getBindingResult()
                            .getAllErrors()
                            .stream()
                            .map(error -> new ParameterErrorInfo(
                                    ((FieldError) error).getField(),
                                    error.getDefaultMessage()
                            ))
                            .collect(Collectors.toList())
                ).orElseGet(null)
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthException.class)
    public Response AuthExceptionHandler(AuthException Error) {
        return new Response(
                ResponseCode.FAIL,
                Error.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(LoginException.class)
    public Response LoginExceptionHandler(LoginException Error) {
        return new Response(
                ResponseCode.FAIL,
                Error.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(IllegalStateException.class)
    public Response IllegalStateExceptionHandler(IllegalStateException Error) {
        return new Response(
                ResponseCode.FAIL,
                Error.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(Exception.class)
    public Response ExceptionHandler(Exception Error) {

        Error.printStackTrace();

        return new Response(
                ResponseCode.FAIL,
                "에러가 발생했습니다. 관리자에게 문의하세요."
        );
    }

}
