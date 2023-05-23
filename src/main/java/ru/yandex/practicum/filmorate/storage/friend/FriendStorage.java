package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {
    void add(Long id, Long friendId);

    void delete(Long id, Long friendId);

    Collection<User> getMutualFriends(Long id, Long otherId);

    Collection<User> getFriendsByUserId(Long id);
}
