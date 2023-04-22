package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Component
@Primary
@Repository
@Slf4j
public class GenreDbStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate)  {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Collection<Genre> getByQuery(String sqlQuery, Object... params) {
        return null;
    }

    @Override
    public Genre add(Genre obj) {
        return null;
    }

    @Override
    public Genre update(Genre obj) {
        return null;
    }

    @Override
    public Genre getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM Genre WHERE genre_id = ?",id);
        if(!userRows.next())
        {
            throw new NoSuchElementException("Не удалось найти жанр");
        }

        Genre genre = Genre.getInstance(
                userRows.getInt("genre_id"),
                userRows.getString("name")
        );
        return genre;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Collection<Genre> getAll(){
        Collection<Genre> genres = jdbcTemplate.query("SELECT * FROM GENRE g order by g.genre_id", new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genre genre = Genre.getInstance(
                        rs.getInt("genre_id"),
                        rs.getString("name")
                );
                return genre;
            }
        });

        return genres;
    }
}
