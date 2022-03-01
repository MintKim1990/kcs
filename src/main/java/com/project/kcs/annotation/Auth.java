package com.project.kcs.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({ TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
}
