package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {

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
    public boolean update(User user) {
        String sql = "UPDATE Users set login = ?, name = ?, email = ?, birthday = ?  where user_id =?";
        int countAffectedRows = jdbcTemplate.update(
                sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return countAffectedRows > 0;
    }

    @Override
    public User getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id=?", id);
        if (!userRows.next()) {
            return null;
        }

        User user = User.getInstance(
                userRows.getLong("user_id"),
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
        Collection<User> users = jdbcTemplate.query("SELECT * FROM Users", (rs, rowNum) -> {
            User user = User.getInstance(
                    rs.getLong("user_id"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("birthday"));

            return user;
        });

        return users;
    }
}
