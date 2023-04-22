package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.IFilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    InMemoryFilmStorage inMemoryFilmStorage;
    IFilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, IFilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел запрос Post /films");
        filmService.add(film);
        log.info("Отправлен ответ" + film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел запрос Put /films");
        Film updatedFilm = filmService.update(film);
        log.info("Отправлен ответ" + updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пришел запрос Put /films/{id}/like/{userId}");
        Film likedFilm = filmService.addLike(id, userId);
        log.info("Отправлен ответ" + likedFilm);
        return likedFilm;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пришел запрос Delete /{id}/like/{userId}");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilm(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        log.info("Пришел запрос Get /popular");
        Collection<Film> topFilms = filmService.getPopularFilms(count);
        log.info("Отправлен ответ" + topFilms);
        return topFilms;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("Пришел запрос Get /{id}");
        Film film = filmService.getFilm(id);
        log.info("Отправлен ответ" + film);
        return film;
    }

    @GetMapping
    public Collection<Film> getFilmList() {
        log.info("Пришел запрос Get /films");
        Collection<Film> allFilms = filmService.getAll();
        log.info("Отправлен ответ" + allFilms);
        return allFilms;
    }
}
