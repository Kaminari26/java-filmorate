package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        log.info("Добавление нового пользователя :" + user);
        String query = "INSERT INTO Users(login, name, email, birthday) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setDate(4, user.getBirthday());
            return preparedStatement;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    @Override
    public User update(User user) {
        int countAffectedRows = jdbcTemplate.update("UPDATE Users set login = ?, name = ?, email = ?, birthday = ?  where user_id =?",
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        if(countAffectedRows == 0)
        {
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        return user;
    }

    @Override
    public User getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id=?",id);
        if(!userRows.next())
        {
            throw new NoSuchElementException("Не удалось найти пользователя");
        }

        User user = User.getInstance(
                userRows.getLong("user_id"),
                new HashSet<>(),
                userRows.getString("login"),
                userRows.getString("name"),
                userRows.getString("email"),
                userRows.getDate("birthday")
                );
         return user;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM Users WHERE user_id = ?", id);
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> users = jdbcTemplate.query("SELECT * FROM Users", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = User.getInstance(
                        rs.getLong("user_id"),
                        new HashSet<>(),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birthday"));
                return user;
            }
        });

        return users;
    }

    @Override
    public Collection<User> getByQuery(String sqlQuery, @Nullable Object ... params) {
        Collection<User> users = jdbcTemplate.query(sqlQuery , new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = User.getInstance(
                        rs.getLong("user_id"),
                        new HashSet<>(),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birthday"));
                return user;
            }
        });
        return  users;
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        checkUsers(id, friendId);
          jdbcTemplate.update("INSERT INTO FRIENDS_USER (user_id, friends_id) VALUES (?,?)", id, friendId);
       return getById(id);
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {
        jdbcTemplate.update("DELETE FROM FRIENDS_USER WHERE user_id = ? AND friends_id = ?", id, friendId);
        return getById(id);
    }

    @Override
    public Collection<User> mutualFriends(Long id, Long otherId) {
        String sql = "SELECT u.* FROM FRIENDS_USER fu \n" +
                "INNER JOIN FRIENDS_USER fu2 ON FU.USER_ID = ? AND FU2.USER_ID = ? AND FU2 .FRIENDS_ID = FU.FRIENDS_ID\n" +
                "INNER JOIN USERS u ON fu .FRIENDS_ID = u.USER_ID";

        Collection<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = User.getInstance(
                        rs.getLong("user_id"),
                        new HashSet<>(),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birthday"));
                return user;
            }
        },id,otherId);
        return users;
    }
    public Collection<User> friendsList( Long id) {
return null;
    }
    public void checkUsers (Long id, Long friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?",id);
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?",friendId);
        if(!userRows.next() || !friendsRows.next())
        {
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
    }
}
