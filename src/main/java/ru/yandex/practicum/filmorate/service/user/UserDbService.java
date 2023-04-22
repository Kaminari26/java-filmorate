package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    public User addFriend(Long id , Long friendId) {
        log.info("Добавление в друзья {} пользователя {} :",id,friendId);
        return userStorage.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей пользователем {} пользователя {} :",id,friendId);
        return userStorage.deleteFriend(id,friendId);
    }
    @Override
    public Collection<User> mutualFriends(Long id, Long otherId) {

       // String sql = "SELECT * FROM USERS WHERE user_id IN (SELECT friends_id FROM FRIENDS_USER WHERE user_id = " + id +" ) AND user_id IN (SELECT friends_id FROM FRIENDS_USER WHERE user_id = "+otherId+")";

        return userStorage.mutualFriends(id, otherId);

    }

    @Override
    public Collection<User> friendsListUsers(Long id) {
               String sql = "SELECT * FROM Users WHERE user_id IN (SELECT friends_id FROM FRIENDS_USER WHERE user_id = "+ id +")";
      //  String sql =  "SELECT u.* FROM FRIENDS_USER f LEFT JOIN users u ON f.friends_id = u.id" +
       //         " WHERE f.userId = "+id;
      //  String sql = "SELECT *  " +
       //        "FROM Users AS us  " +
       //         "LEFT JOIN FRIENDS_USER AS fu ON us.USER_ID = FU.USER_ID " +
        //        "WHERE us.USER_ID = "+id+";";


        return  userStorage.getByQuery(sql);
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

