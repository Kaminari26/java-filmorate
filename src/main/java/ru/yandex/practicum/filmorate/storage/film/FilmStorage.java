package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;


public interface FilmStorage extends Storage<Film> {

    Collection<Film> getPopularFilms(int count);

    Film add(Film film);

    boolean update(Film film);

    Film getById(Long id);

    void delete(Long id);

    Collection<Film> getAll();

}
