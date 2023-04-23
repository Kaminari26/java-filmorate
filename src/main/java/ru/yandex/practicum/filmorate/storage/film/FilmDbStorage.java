package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

@Component
@Primary
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film add(Film film) {
        String query = "INSERT INTO FILMS (name, description, release_date, duration, mpa_id) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, film.getReleaseDate());
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());

            return preparedStatement;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        return film;
    }

    @Override
    public boolean update(Film film) {
        String sql = "UPDATE FILMS set name = ?, description = ?, release_date = ?, duration = ?,  mpa_id =? WHERE film_Id = ?";

        int countAffectedRows = jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        return countAffectedRows > 0;
    }

    @Override
    public Film getById(Long id) {
        String sql = "SELECT f.* , m.NAME AS mpa_name FROM FILMS f  LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID WHERE f.film_Id = " + id + ";";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql);
        if (!userRows.next()) {
            return null;
        }

        Film film = Film.getInstance(
                userRows.getLong("film_Id"),
                userRows.getString("name"),
                userRows.getString("description"),
                userRows.getDate("release_date"),
                userRows.getInt("duration"),
                MpaRating.getInstance(userRows.getInt("MPA_ID"), userRows.getString("MPA_NAME")),
                null
        );

        return film;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE film_Id = ?", id);
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT F.*, AP.NAME AS MPA_name " +
                "FROM FILMS AS F " +
                "JOIN MPA AS ap ON  F.MPA_ID = AP .MPA_ID ";
        Collection<Film> films = jdbcTemplate.query(sql, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = Film.getInstance(
                        rs.getLong("film_Id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date"),
                        rs.getInt("duration"),
                        MpaRating.getInstance(rs.getInt("mpa_id"), rs.getString("MPA_name")),
                        null
                );

                return film;
            }
        });

        return films;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        String sql = "SELECT f.* , COUNT(lf.FILM_ID), ap.NAME AS MPA_Name " +
                "FROM FILMS f " +
                "LEFT JOIN LIKES_FILM lf ON f.FILM_ID = lf.FILM_ID " +
                "LEFT JOIN MPA AS ap ON  f.MPA_ID = AP .MPA_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(lf.FILM_ID) DESC \n" +
                "LIMIT ?";

        Collection<Film> films = jdbcTemplate.query(sql, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = Film.getInstance(
                        rs.getLong("film_Id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date"),
                        rs.getInt("duration"),
                        MpaRating.getInstance(rs.getInt("mpa_id"), rs.getString("MPA_name")),
                        null
                );

                return film;
            }
        }, count);

        return films;
    }
}
