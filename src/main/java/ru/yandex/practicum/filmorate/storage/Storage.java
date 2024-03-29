package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {
    T add(T obj);

    boolean update(T obj);

    T getById(Long id);

    void delete(Long id);

    Collection<T> getAll();
}