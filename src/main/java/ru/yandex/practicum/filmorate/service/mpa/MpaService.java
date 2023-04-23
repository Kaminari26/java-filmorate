package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@Primary
@Slf4j
public class MpaService implements IMpaService {

    MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MpaRating getRatingId(Long id) {
        MpaRating mpaRating = mpaStorage.getById(id);
        if (mpaRating == null) {
            throw new NoSuchElementException("Не удалось найти рейтинг");
        }

        return mpaRating;
    }

    @Override
    public Collection<MpaRating> getAllMpa() {
        return mpaStorage.getRatingAll();
    }
}
