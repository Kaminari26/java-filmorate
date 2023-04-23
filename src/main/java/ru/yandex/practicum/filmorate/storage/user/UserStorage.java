package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

public interface UserStorage extends Storage<User> {

    User add(User user);

    boolean update(User user);

    User getById(Long id);

    void delete(Long id);

    Collection<User> getAll();

}
