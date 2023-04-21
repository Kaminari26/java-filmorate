package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DbStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;


public interface FilmStorage extends DbStorage<Film> {
    public Film addLike(Long id, Long userId);
    public void deleteLike(Long id, Long userId);
    public Collection<Film> getPopularFilms(int count);
}
