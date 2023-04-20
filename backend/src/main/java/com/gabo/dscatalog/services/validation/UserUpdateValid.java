package com.gabo.dscatalog.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UserUpdateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})

public @interface UserUpdateValid {
	String message() default "Validation error";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
