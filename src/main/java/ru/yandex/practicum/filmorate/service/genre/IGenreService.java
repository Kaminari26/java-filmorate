package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface IGenreService {
    Collection<Genre> getAllGenres();

    Genre getGenreById(Long id);
}
