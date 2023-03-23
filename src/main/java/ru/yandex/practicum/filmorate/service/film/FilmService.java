package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film addLike(Long id, Long userId) {
        if (!inMemoryFilmStorage.getFilmMap().containsKey(id)) {
            log.error("Фильм с id " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("Пользователь: "
                + inMemoryUserStorage.getUsersMap().get(userId)
                + " ставит лайк фильму: "
                + inMemoryFilmStorage.getFilmMap().get(id));

        if (inMemoryFilmStorage.getFilmMap().get(id).getLikeList().equals(userId)) {
            log.warn("Лайк уже был поставлен ранее");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED);
        } else {
            log.info("Лайк успешно поставлен");
            inMemoryFilmStorage.getFilmMap().get(id).getLikeList().add(userId);
            return inMemoryFilmStorage.getFilmMap().get(id);
        }
    }

    public void deleteLike(Long id, Long userId) {
        if (!inMemoryFilmStorage.getFilmMap().containsKey(id)) {
            log.error("Фильм с id " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("Пользователь: "
                + id
                + " удаляет лайк у фильма: "
                + inMemoryFilmStorage.getFilmMap().get(id));
        if (!inMemoryFilmStorage.getFilmMap().get(id).getLikeList().contains(userId)) {
            log.warn("Лайк не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            log.info("Лайк был удален");
            inMemoryFilmStorage.getFilmMap().get(id).getLikeList().remove(userId);
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
        if(!inMemoryFilmStorage.getFilmMap().containsKey(id)) {
            log.error("Фильм не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
            log.info("Фильм найден");
            return inMemoryFilmStorage.getFilmMap().get(id);
    }
}
