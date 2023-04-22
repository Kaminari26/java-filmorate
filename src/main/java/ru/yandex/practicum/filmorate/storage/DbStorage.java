package ru.yandex.practicum.filmorate.storage;

import org.springframework.lang.Nullable;

import java.util.Collection;

public interface DbStorage<T> extends Storage<T> {
    Collection<T> getByQuery(String sqlQuery, @Nullable Object... params);
}
