package ru.yandex.practicum.filmorate.сontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage);


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
       inMemoryFilmStorage.add(film);
       return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Long id, @PathVariable Long userId) {
       return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilm(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count){
           return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
      return filmService.getFilm(id);
    }

    @GetMapping
    public Collection<Film> getFilmList() {
        return inMemoryFilmStorage.getAll();
    }
}
