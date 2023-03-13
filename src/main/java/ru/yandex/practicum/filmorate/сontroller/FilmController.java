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
    private int counter;
    public static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895,12,28);
    private final Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрошено добавление фильма " + film);
        if (film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            log.warn("Неверно указана дата релиза" + film.getName());
            throw new ValidationException("Произошла ошибка при попытке создания фильма");
        } else {
            film.setId(++counter);
            filmMap.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " добавлен.");
            return film;
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма " + film);
        if (!filmMap.containsKey(film.getId())) {
            throw new RuntimeException("Не удалось обновить фильм");
        }
        filmMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " обновлен");
        return film;
    }

    @GetMapping
    public Collection<Film> getFilmList() {
        log.info("Запрошен список фильмов");
        return filmMap.values();
    }
}
