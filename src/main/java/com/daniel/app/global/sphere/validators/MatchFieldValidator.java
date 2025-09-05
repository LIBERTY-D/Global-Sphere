package com.daniel.app.global.sphere.validators;


import com.daniel.app.global.sphere.annotation.MatchFieldValueAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class MatchFieldValidator implements ConstraintValidator<MatchFieldValueAnnotation,
        Object> {

    String field;
    String fieldMatch;

    @Override
    public void initialize(MatchFieldValueAnnotation constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldValueMatch = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        boolean valid = (fieldValue == null && fieldValueMatch == null)
                || (fieldValue != null && fieldValue.equals(fieldValueMatch));

        if (!valid) {
            // disable the default "class-level" violation
            context.disableDefaultConstraintViolation();
            // add error to the confirmPassword field
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(fieldMatch) //  attaches to confirmPassword
                    .addConstraintViolation();
        }

        return valid;
    }

}
