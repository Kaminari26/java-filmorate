package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface IUserService {
    User addFriend (Long id , Long friendId);

    User deleteFriend(Long id, Long friendId);

    Collection<User> mutualFriends(Long id, Long otherId);

    Collection<User> friendsListUsers(Long id);

    User addUser(User user);
    User update(User user);
    User getById(Long id);
    Collection<User> getAll();
}
