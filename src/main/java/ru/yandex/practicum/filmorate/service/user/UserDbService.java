package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Primary
@Slf4j
public class UserDbService implements IUserService {
    UserStorage userStorage;

    @Autowired
    public UserDbService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public User addFriend(Long id, Long friendId) {
        log.info("Добавление в друзья {} пользователя {} :", id, friendId);
        return userStorage.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей пользователем {} пользователя {} :", id, friendId);
        return userStorage.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> mutualFriends(Long id, Long otherId) {

        return userStorage.mutualFriends(id, otherId);

    }

    @Override
    public Collection<User> friendsListUsers(Long id) {
        String sql = "SELECT * FROM Users WHERE user_id IN (SELECT friends_id FROM FRIENDS_USER WHERE user_id = " + id + ")";

        return userStorage.getByQuery(sql);
    }

    @Override
    public User addUser(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User getById(Long id) {
        return userStorage.getById(id);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }
}

