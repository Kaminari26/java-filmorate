package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MemoryStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements IUserService {
    private final MemoryStorage<User> userStorage;

    @Autowired
    public UserService(MemoryStorage<User> inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public User addFriend(Long id, Long friendId) {
        if (userStorage.getMap().get(id) == null || userStorage.getMap().get(friendId) == null) {
            log.error("Пользователь не найден");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }

        log.info("Пользователь " + id + " добавляет в друзья " + friendId);
        userStorage.getMap().get(id).getFriendsIds().add(friendId);
        userStorage.getMap().get(friendId).getFriendsIds().add(id);
        log.info("Друзья добавлены");
        return userStorage.getMap().get(id);
    }

    public User deleteFriend(Long id, Long friendId) {
        if (userStorage.getMap().get(id) == null || userStorage.getMap().get(friendId) == null) {
            log.error("Пользователь не найден");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        log.info("Пользователь " + id + " удаляет из друзей " + friendId);
        userStorage.getMap().get(id).getFriendsIds().remove(friendId);
        userStorage.getMap().get(friendId).getFriendsIds().remove(id);
        log.info("Друг удален");
        return userStorage.getMap().get(id);
    }

    public Collection<User> mutualFriends(Long id, Long otherId) {
        if (userStorage.getMap().get(id) == null || userStorage.getMap().get(otherId) == null) {
            log.error("Пользователь не найден");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        Set<Long> mutualFriendsId = userStorage.getMap().get(id).getFriendsIds().stream().
                filter(userStorage.getMap().get(otherId).getFriendsIds()::contains).collect(Collectors.toSet());
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (Long friendsId : mutualFriendsId) {
            mutualFriends.add(userStorage.getMap().get(friendsId));
        }
        log.info("Список общих друзей получен");
        return mutualFriends;
    }

    public Collection<User> friendsListUsers(Long id) {
        Collection<User> users = new ArrayList<>();
        for (Long friends : userStorage.getById(id).getFriendsIds()) {
            users.add(userStorage.getById(friends));
        }
        log.info("Список друзей получен");
        return users;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    private boolean contains(Long id) {
        return userStorage.getMap().containsKey(id);
    }
}
