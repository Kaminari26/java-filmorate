package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.List;

public interface MpaStorage extends DbStorage<MpaRating> {
    public MpaRating getRating(int id);

    List<MpaRating> getRatingAll();

    MpaRating getRatingById(int id);
}


