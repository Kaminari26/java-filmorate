package ru.yandex.practicum.filmorate.сontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/film")
@Slf4j
public class FilmController {
    Validation validation = new Validation();
    private final Map<String, Film> filmMap = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        Film validFilm = validation.filmValidation(film);
        filmMap.put(validFilm.getName(), validFilm);
        log.info("Фильм " + validFilm.getName() + " добавлен.");
        return validFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        Film validFilm = validation.filmValidation(film);
        if (filmMap.containsKey(validFilm.getName())) {
            filmMap.put(validFilm.getName(), validFilm);
        }
        log.info("Фильм " + validFilm.getName() + " обновлен");
        return validFilm;
    }

    @GetMapping
    public Map getFilmList(){
        return  filmMap;
    }
}
