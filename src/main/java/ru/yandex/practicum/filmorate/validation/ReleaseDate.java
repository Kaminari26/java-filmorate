package ru.yandex.practicum.filmorate.validation;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateReleaseValidation.class)
@Documented

public @interface ReleaseDate {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] Payload() default {};
}
