package com.gabo.dscatalog.services.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UserInsertValidator.class)
@Retention(RetentionPolicy.RUNTIME)

public @interface UserInsertValid {
	String message() default "Validation error";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
