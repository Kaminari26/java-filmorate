package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

public interface UserStorage extends DbStorage<User> {
    public User addFriend(Long id , Long friendId);
    public User deleteFriend(Long id, Long friendId);
    public Collection<User> mutualFriends(Long id, Long otherId);
}
