package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Long counter = 0l;
    public static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895,12,28);
    private final HashMap<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film add(Film film) {
        log.info("Запрошено добавление фильма " + film);
        if (film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            log.error("Неверно указана дата релиза" + film.getName());
            throw new ValidationException("Произошла ошибка при попытке создания фильма");
        }
        film.setId(++counter);
        filmMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " добавлен.");
        return film;
    }

    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма " + film);
        if (!filmMap.containsKey(film.getId())) {
            throw new RuntimeException("Не удалось обновить фильм");
        }
        filmMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " обновлен");
        return film;
    }

    @Override
    public Film getById(Long id) {
        log.info("Запрос фильма");
        if(!contains(id)) {
            log.warn("Фильм не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return filmMap.get(id);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление фильма " + id);
        if(!contains(id)){
            log.warn("Фильм не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("Фильм удален");
        filmMap.remove(id);
    }

    @Override
    public Collection<Film> getAll() {
        log.info("Запрошен список фильмов");
        return filmMap.values();
    }

    @Override
    public Boolean contains(Long id) {
       return filmMap.containsKey(id);
    }

    public HashMap<Long, Film> getFilmMap() {
        return filmMap;
    }
}
