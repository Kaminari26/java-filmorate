package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface IFilmService {
    Film addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getPopularFilms(int count);

    Film getFilm(Long id);

    Film add(Film film);

    Film update(Film film);

    Film getById(Long id);

    void delete(Long id);

    Collection<Film> getAll();

}
