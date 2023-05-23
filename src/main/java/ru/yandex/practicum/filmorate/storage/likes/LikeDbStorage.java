package ru.yandex.practicum.filmorate.storage.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Primary
@Repository
@Slf4j
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Long id, Long userId) {
        String sql = "INSERT INTO LIKES_FILM (film_id, user_id)" +
                " values(?,?)";

        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void delete(Long id, Long userId) {
        jdbcTemplate.update("DELETE FROM LIKES_FILM WHERE film_id = ? AND user_id = ?", id, userId);
    }
}
