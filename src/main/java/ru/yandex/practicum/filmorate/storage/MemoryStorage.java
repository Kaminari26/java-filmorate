package ru.yandex.practicum.filmorate.storage;

import java.util.Map;

public interface MemoryStorage<T> extends Storage<T> {
    Map<Long, T> getMap();
}
