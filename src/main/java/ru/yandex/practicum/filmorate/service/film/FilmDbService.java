package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@Primary
@Slf4j
public class FilmDbService implements IFilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmDbService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film addLike(Long id, Long userId) {

        return filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id,userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    @Override
    public Film getFilm(Long id) {
        return filmStorage.getById(id);
    }

    @Override
    public Film add(Film film) {
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    @Override
    public void delete(Long id) {
        filmStorage.delete(id);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }
}
