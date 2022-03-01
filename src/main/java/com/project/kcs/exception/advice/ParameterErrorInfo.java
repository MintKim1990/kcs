package com.project.kcs.exception.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParameterErrorInfo {

    private String parameterName;
    private String message;

}
