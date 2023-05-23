package ru.yandex.practicum.filmorate.storage.likes;

public interface LikeStorage {
    void add(Long id, Long userId);

    void delete(Long id, Long userId);
}
