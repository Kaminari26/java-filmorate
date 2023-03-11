package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class Validation implements ConstraintValidator<NoSpaces, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return !str.contains(" ");
    }

}
