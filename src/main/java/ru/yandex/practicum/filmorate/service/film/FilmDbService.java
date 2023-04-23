package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
@Primary
@Slf4j
public class FilmDbService implements IFilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmDbService(FilmStorage filmStorage, GenreStorage genreStorage, LikeStorage likeStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long id, Long userId) {
        likeStorage.add(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NoSuchElementException("Не удалось найти такого пользователя");
        }

        likeStorage.delete(id, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        if (count <= 0) {
            count = 10;
        }

        Collection<Film> films = filmStorage.getPopularFilms(count);
        for (Film film : films) {
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
        }

        return films;
    }

    @Override
    public Film getFilm(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NoSuchElementException("Не удалось найти фильм");
        }

        film.setGenres(genreStorage.getGenresByFilmId(film.getId()));

        return film;
    }

    @Override
    public Film add(Film film) {
        Date movieBirthday = Date.valueOf(LocalDate.of(1895, 12, 28));
        log.info("Добавление нового фильма :" + film);
        if (film.getReleaseDate().before(movieBirthday)) {
            log.error("Неверно указана дата релиза " + film.getName());
            throw new ValidationException("Произошла ошибка при попытке создания фильма");
        }

        filmStorage.add(film);
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            genreStorage.add(film.getGenres(), film.getId());
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        boolean isUpdate = filmStorage.update(film);
        if (!isUpdate) {
            throw new NoSuchElementException("Не удалось найти фильм");
        }

        genreStorage.add(film.getGenres(), film.getId());
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            Comparator<Genre> comparator = Comparator.comparing(genre -> genre.getId());
            Set<Genre> sortedGenresByIdAsc = new TreeSet<>(comparator);
            sortedGenresByIdAsc.addAll(film.getGenres());

            film.setGenres(sortedGenresByIdAsc);
        }

        return film;
    }

    @Override
    public Film getById(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NoSuchElementException("Не удалось найти фильм");
        }

        film.setGenres(genreStorage.getGenresByFilmId(id));
        return film;
    }

    @Override
    public void delete(Long id) {
        filmStorage.delete(id);
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = filmStorage.getAll();

        for (Film film : films) {
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
        }

        return films;
    }
}
