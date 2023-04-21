package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.MemoryStorage;


import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class InMemoryFilmStorage implements MemoryStorage<Film> {
    private Long counter = 0l;
    public static final Date MOVIE_BIRTHDAY = Date.valueOf(LocalDate.of(1895,12,28));
    private final HashMap<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film add(Film film) {
        log.info("Запрошено добавление фильма " + film);
        if (!film.getReleaseDate().before(MOVIE_BIRTHDAY)) {
            log.error("Неверно указана дата релиза " + film.getName());
            throw new ValidationException("Произошла ошибка при попытке создания фильма");
        }

        film.setId(++counter);
        filmMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " добавлен.");
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма {}", film);

        if (!filmMap.containsKey(film.getId())) {
            throw new NoSuchElementException("Не удалось обновить фильм");
        }

        filmMap.put(film.getId(), film);
        log.info("Фильм {} обновлен", film.getName());
        return film;
    }

    @Override
    public Film getById(Long id) {
        log.info("Запрос фильма {}", id);

        Film film = filmMap.get(id);

        if(film == null) {
            log.error("Фильм {} не найден", id);
            throw new NoSuchElementException(String.format("Не удалось найти Фильм %s", id));
        }

        return film;
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление фильма " + id);

        if(!filmMap.containsKey(id)) {
            log.error("Фильм не найден");
            throw new NoSuchElementException("Не удалось найти Фильм");
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
    public Map<Long, Film> getMap() {
        return filmMap;
    }
}
