package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.IGenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {

    IGenreService genreService;

    @Autowired
    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public Genre getRating(@PathVariable long id) {
        log.info("Пришел запрос /genres/{id}");
        Genre genre = genreService.getGenreById(id);
        log.info("ответ " + genre);
        return genre;
    }

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("Пришел запрос /genres");
        Collection<Genre> genres = genreService.getAllGenres();
        log.info("ответ " + genres);
        return genres;
    }
}
