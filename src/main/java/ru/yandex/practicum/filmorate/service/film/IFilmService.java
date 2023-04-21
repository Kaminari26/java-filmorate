package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface IFilmService {
    public Film addLike(Long id, Long userId);
    public void deleteLike(Long id, Long userId);
    public Collection<Film> getPopularFilms(int count);
    public Film getFilm (Long id);
    public Film add(Film film);
    public Film update(Film film);
    public Film getById(Long id);
    public void delete(Long id);
    public Collection<Film> getAll();

}
