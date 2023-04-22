package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film addLike(Long id, Long userId) {
        Film film = inMemoryFilmStorage.getById(id);

        log.info("Пользователь: {} ставит лайк фильму: {}", inMemoryUserStorage.getMap().get(userId), film);

        if (film.getLikes().equals(userId)) {
            log.warn("Лайк уже был поставлен ранее");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED);
        }
        log.info("Лайк успешно поставлен");
        film.getLikes().add(userId);
        return film;
    }

    public void deleteLike(Long id, Long userId) {
        Film film = inMemoryFilmStorage.getById(id);
        log.info("Пользователь: {} удаляет лайк у фильма: {}", inMemoryUserStorage.getMap().get(userId), film);
        if (!film.getLikes().contains(userId)) {
            log.warn("Лайк не найден");
            throw new NoSuchElementException("Не удалось найти лайк");
        }
        log.info("Лайк был удален");
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрошен топ фильмов");
        Collection<Film> popularFilms = inMemoryFilmStorage.getAll();
        return popularFilms.stream()
                .sorted((film1, film2) -> film2.getAmountOfLikes() - film1.getAmountOfLikes())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(Long id) {
        log.info("Запрошен фильм с id " + id);
        Film film = inMemoryFilmStorage.getById(id);
        log.info("Фильм найден");
        return film;
    }
}
