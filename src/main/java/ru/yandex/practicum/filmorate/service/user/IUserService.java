package ru.yandex.practicum.filmorate.service.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface IUserService {
    public User addFriend (Long id , Long friendId);

    public User deleteFriend(Long id, Long friendId);

    public Collection<User> mutualFriends(Long id, Long otherId);

    public Collection<User> friendsListUsers(Long id);

    public User addUser(User user);
    public User update(User user);
    public User getById(Long id);
    public Collection<User> getAll();
}
