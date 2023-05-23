package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@Primary
@Slf4j
public class UserDbService implements IUserService {
    private final UserStorage userStorage;
    private final FriendDbStorage friendDbStorage;

    @Autowired
    public UserDbService(UserStorage userStorage, FriendDbStorage friendDbStorage) {
        this.userStorage = userStorage;
        this.friendDbStorage = friendDbStorage;
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        log.info("Добавление в друзья {} пользователя {} :", id, friendId);
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);

        if (user == null || friend == null) {
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        friendDbStorage.add(id, friendId);

        return userStorage.getById(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей пользователем {} пользователя {} :", id, friendId);
        friendDbStorage.delete(id, friendId);
    }

    @Override
    public Collection<User> mutualFriends(Long id, Long otherId) {
        return friendDbStorage.getMutualFriends(id, otherId);
    }

    @Override
    public Collection<User> friendsListUsers(Long id) {
        return friendDbStorage.getFriendsByUserId(id);
    }

    @Override
    public User addUser(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        boolean isUpdated = userStorage.update(user);
        if (!isUpdated) {
            throw new NoSuchElementException("Не удалось найти пользователя");
        }

        return user;
    }

    @Override
    public User getById(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NoSuchElementException("Не удалось найти пользователя");
        }

        return user;
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }
}

