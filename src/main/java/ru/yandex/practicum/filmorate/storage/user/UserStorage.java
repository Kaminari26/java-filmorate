package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.Collection;

public interface UserStorage extends DbStorage<User> {
    User addFriend(Long id, Long friendId);

    User deleteFriend(Long id, Long friendId);

    Collection<User> mutualFriends(Long id, Long otherId);
}
