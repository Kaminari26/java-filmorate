package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class Validation {
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate RELEASE_DATE_EARLIEST = LocalDate.of(1995, 12, 28);

    public User userValidation(User user) throws ValidationException {
        if (user.getEmail() != null && user.getEmail().contains("@")) {
            if (user.getLogin() != null && !user.getLogin().contains(" ")) {
                if (LocalDateTime.now().isAfter(user.getBirthday())) {
                    if (user.getName() == null) {
                        user.setName(user.getLogin());
                        return user;
                    } else {
                        return user;
                    }
                }
            }
        }
        throw new ValidationException("Ошбика валидации");
    }

    public Film filmValidation (Film film) throws ValidationException {
    if(film.getName() != null){
        if(film.getDescription().length() <= MAX_LENGTH_DESCRIPTION) {
            if (film.getReleaseDate().isAfter(RELEASE_DATE_EARLIEST)){
                if(film.getDuration() > 0){
                    return film;
                }

            }
        }
    }

        throw new ValidationException("Ошбика валидации");
    }
}
