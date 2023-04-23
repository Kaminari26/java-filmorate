package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface IMpaService {
    MpaRating getRatingId(Long id);

    Collection<MpaRating> getAllMpa();
}
