package ru.yandex.practicum.filmorate.storage.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Primary
@Repository
@Slf4j
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Long id, Long friendId) {
        jdbcTemplate.update("INSERT INTO FRIENDS_USER (user_id, friend_id) VALUES (?,?)", id, friendId);
    }

    @Override
    public void delete(Long id, Long friendId) {
        jdbcTemplate.update("DELETE FROM FRIENDS_USER WHERE user_id = ? AND friend_id = ?", id, friendId);
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        String sql = "SELECT u.* FROM FRIENDS_USER fu \n" +
                "INNER JOIN FRIENDS_USER fu2 ON FU.USER_ID = ? AND FU2.USER_ID = ? AND FU2 .FRIEND_ID = FU.FRIEND_ID\n" +
                "INNER JOIN USERS u ON fu .FRIEND_ID = u.USER_ID";

        Collection<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = User.getInstance(
                        rs.getLong("user_id"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birthday"));
                return user;
            }
        }, id, otherId);
        return users;
    }

    @Override
    public Collection<User> getFriendsByUserId(Long id) {
        String sql = "SELECT u.* FROM FRIENDS_USER fu" +
                " INNER JOIN USERS u on u.user_id = fu.friend_id and fu.user_id = ?";

        Collection<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = User.getInstance(
                        rs.getLong("user_id"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birthday"));
                return user;
            }
        }, id);

        return users;
    }
}
