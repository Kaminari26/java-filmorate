package ru.yandex.practicum.filmorate.service.Mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.util.Collection;

@Service
@Primary
@Slf4j
public class MpaService implements IMpaService {

    MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MpaRating getRatingId(int id) {

        return mpaStorage.getRatingById(id);
    }

    @Override
    public Collection<MpaRating> getAllMpa() {
        return mpaStorage.getAll();
    }
}
