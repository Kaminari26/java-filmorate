package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validation;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidTest extends Validation {



    @Test
    void filmVaildTest() throws ValidationException {
        Film film = new Film( 1, "Название","Что то происходит", LocalDate.now(), 120);
        assertEquals(film, filmValidation(film),"Ошибка при валидации фильма");

    }
}
