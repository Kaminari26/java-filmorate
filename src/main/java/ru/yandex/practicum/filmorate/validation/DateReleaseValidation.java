package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateReleaseValidation implements ConstraintValidator<ReleaseDate, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate localDate = LocalDate.parse(s);
        LocalDate localDateFilm = LocalDate.of(1895,12,28);
        return localDate.isAfter(localDateFilm);
    }
}
