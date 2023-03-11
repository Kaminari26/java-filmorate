package ru.yandex.practicum.filmorate.сontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<String, Film> filmMap = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        filmMap.put(film.getName(), film);
        log.info("Фильм " + film.getName() + " добавлен.");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (filmMap.containsKey(film.getName())) {
            filmMap.put(film.getName(), film);
        }
        log.info("Фильм " + film.getName() + " обновлен");
        return film;
    }

    @GetMapping
    public Map getFilmList(){
        return  filmMap;
    }
}
