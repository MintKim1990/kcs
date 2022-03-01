package com.project.kcs.annotation;

import com.project.kcs.validator.GenderValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

@Constraint(validatedBy = GenderValidator.class)
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Gender {

    String message() default "남성 : 1, 여성 : 2 값으로 입력해 주세요.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
