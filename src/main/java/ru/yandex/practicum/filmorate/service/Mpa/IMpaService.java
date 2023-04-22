package ru.yandex.practicum.filmorate.service.Mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface IMpaService {
    MpaRating getRatingId(int id);

    Collection<MpaRating> getAllMpa();
}
