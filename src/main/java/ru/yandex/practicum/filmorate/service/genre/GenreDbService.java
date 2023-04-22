package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@Primary
@Slf4j
public class GenreDbService implements IGenreService {

    GenreStorage genreStorage;

    @Autowired
    public GenreDbService(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreStorage.getById(id);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

}
