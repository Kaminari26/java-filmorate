package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface IGenreService {
    Collection<Genre> getAll();

    Genre getGenre(Long id);
}
