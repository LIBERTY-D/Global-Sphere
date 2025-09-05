package com.daniel.app.global.sphere.annotation;


import com.daniel.app.global.sphere.validators.MatchFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Constraint(validatedBy = MatchFieldValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchFieldValueAnnotation {
    String field();
    String fieldMatch();
    String message() default "Fields do not match";

    Class <?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface  Fields{
        MatchFieldValueAnnotation [] value();
    }

}
