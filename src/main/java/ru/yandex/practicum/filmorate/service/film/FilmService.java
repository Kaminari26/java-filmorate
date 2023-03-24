package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film addLike(Long id, Long userId) {
        Film film = inMemoryFilmStorage.getById(id);

        log.info("Пользователь: {} ставит лайк фильму: {}", inMemoryUserStorage.getUsersMap().get(userId), film);

        if (film.getLikes().equals(userId)) {
            log.warn("Лайк уже был поставлен ранее");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED);
        } else {
            log.info("Лайк успешно поставлен");
            film.getLikes().add(userId);
            return film;
        }
    }

    public void deleteLike(Long id, Long userId) {
        Film film = inMemoryFilmStorage.getById(id);
        log.info("Пользователь: {} удаляет лайк у фильма: {}", inMemoryUserStorage.getUsersMap().get(userId), film);
        if (!film.getLikes().contains(userId)) {
            log.warn("Лайк не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            log.info("Лайк был удален");
            film.getLikes().remove(userId);
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрошен топ фильмов");
        Collection<Film> popularFilms = inMemoryFilmStorage.getAll();
        return popularFilms.stream()
                .sorted((film1,film2) -> film2.getAmountOfLikes() - film1.getAmountOfLikes())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm (Long id) {
        log.info("Запрошен фильм с id " + id);
        Film film = inMemoryFilmStorage.getById(id);
        log.info("Фильм найден");
        return film;
    }
}
