package com.project.kcs.validator;

import com.project.kcs.annotation.Gender;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class GenderValidator implements ConstraintValidator<Gender, String> {

    private final List<String> parameterList = Arrays.asList("1", "2");

    @Override
    public void initialize(Gender constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.hasText(value))
            return parameterList.stream().anyMatch(parameter -> parameter.equals(value));

        return true;
    }

}
