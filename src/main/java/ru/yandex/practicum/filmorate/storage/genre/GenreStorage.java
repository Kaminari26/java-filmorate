package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;


public interface GenreStorage {
    Genre getById(Long id);

    Collection<Genre> getAll();

    Set<Genre> getGenresByFilmId(Long id);

    void add(Set<Genre> genres, Long filmId);

}
