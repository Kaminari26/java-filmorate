package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.Collection;


public interface FilmStorage extends DbStorage<Film> {
    Film addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getPopularFilms(int count);
}
