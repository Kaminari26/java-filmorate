package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id",id);
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
    public Collection<Genre> getAll() {
        return null;
    }
}
