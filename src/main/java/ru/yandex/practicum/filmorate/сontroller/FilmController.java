package ru.yandex.practicum.filmorate.сontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    LocalDate movieBirthday = LocalDate.of(1895,12,28);
    private final Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            throw new ValidationException("500");
        } else {
            film.setId(film.filmId());
            filmMap.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " добавлен.");
            return film;
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " обновлен");
            return film;
        } else {
            throw new RuntimeException("500");
        }
    }

    @GetMapping
    public Collection<Film> getFilmList(){
        return  filmMap.values();
    }
}
