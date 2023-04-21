package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
@Primary
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate)  {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film add(Film film) {
        log.info("Добавление нового фильма :" + film);
        try{
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

        Integer filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        if (film.getGenres() != null) {
            String sqlGenres = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenres, filmId, genre.getId());
            }
        }
        log.info("Фильм {} добавлен, id={}", film.getName(), filmId);
    } catch (
    DataAccessException e) {
        log.warn("Ошибка добавления фильма {}, id={}", film.getName(), film.getId());
        return null;
    }
        return film;
    }

    @Override
    public Film update(Film film) {
        int countAffectedRows = jdbcTemplate.update("UPDATE FILMS set name = ?, description = ?, release_date = ?, duration = ?,  mpa_id =? WHERE film_Id = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (countAffectedRows == 0) {
            throw new NoSuchElementException("Не удалось найти фильм");
        }
        return film;

    }

    @Override
    public Film getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT f.* , m.NAME AS mpa_name FROM FILMS f WHERE film_Id = ? INNER JOIN MPA m ON f.MPA_ID = m.MPA_ID ", id);
        if (!userRows.next()) {
            throw new NoSuchElementException("Не удалось найти фильм");
        }

        Film film = Film.getInstance(
                userRows.getLong("film_Id"),
                new HashSet<>(),
                userRows.getString("name"),
                userRows.getString("description"),
                userRows.getDate("release_date"),
                userRows.getInt("duration"),
                 new MpaRating(userRows.getInt("mpa_id"),userRows.getString("mpa_name")),
                null
        );

        film.setGenres(getGenreSets(id));
        log.info("Фильм {} получен по id={}", film.getName(), id);
        return film;

    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE film_Id = ?", id);
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = jdbcTemplate.query("SELECT F.*, AP.NAME AS MPA_name " +
                "FROM FILMS AS F " +
                "JOIN MPA AS ap ON  F.MPA_ID = AP .MPA_ID ", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = Film.getInstance(
                        rs.getLong("film_Id"),
                        new HashSet<>(),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date"),
                        rs.getInt("duration"),
                        new MpaRating(rs.getInt("mpa_id"),rs.getString("MPA_name")),
                        getGenreSets(rs.getLong("film_Id"))
                );
                return film;
            }
        });

        return films;

    }

    @Override
    public Collection<Film> getByQuery(String sqlQuery, @Nullable Object ... params) {

//        Collection<Film> films = jdbcTemplate.query(sqlQuery, new RowMapper<Film>() {
//            @Override
//            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Film film = Film.getInstance(
//                        rs.getLong("film_Id"),
//                        new HashSet<>(),
//                        rs.getString("name"),
//                        rs.getString("description"),
//                        rs.getDate("release_date"),
//                        rs.getInt("duration"),
//                        rs.getInt("mpa_id")
//                );
//                return film;
//            }
//        }, params);

        return null;
    }

    @Override
    public Film addLike(Long id, Long userId) {
        return null;
    }

    @Override
    public void deleteLike(Long id, Long userId) {

    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        int countFilms = count;
        if (countFilms == 0) {
            countFilms = 10;
        }
        String sql = "SELECT f.* , COUNT(lf.FILM_ID), ap.NAME AS MPA_Name " +
                "FROM FILMS f " +
                "LEFT JOIN LIKES_FILM lf ON f.FILM_ID = lf.FILM_ID " +
                "JOIN MPA AS ap ON  f.MPA_ID = AP .MPA_ID " +
                "GROUP BY f.FILM_ID " +
        "ORDER BY COUNT(lf.FILM_ID)\n" +
                "LIMIT " + countFilms;

        Collection<Film> films = jdbcTemplate.query(sql, new RowMapper<Film>() {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = Film.getInstance(
                    rs.getLong("film_Id"),
                    new HashSet<>(),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date"),
                    rs.getInt("duration"),
                    new MpaRating(rs.getInt("mpa_id"),rs.getString("MPA_name")),
                    getGenreSets(rs.getLong("film_Id"))
            );
            return film;
        }
    });

        return films;
    }

    public String getRatingName(long id) {
        String str = "SELECT name FROM MPA WHERE mpa_id = " + id;
        String rating = jdbcTemplate.queryForRowSet(str).toString();
        return rating;
    }
    public Set<Genre> getGenreSets (long id){
        String sql = "SELECT  fg.GENRE_ID , genre.NAME AS genre_Name " +
                "FROM GENRE  " +
                "JOIN FILM_GENRE fg ON Genre.GENRE_ID = fg.GENRE_ID " +
                "JOIN FILMS f ON f.FILM_ID = fg.FILM_ID WHERE f.FILM_ID = " + id;
        Collection<Genre> genres = jdbcTemplate.query(sql, new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genre genre = Genre.getInstance(
                        rs.getInt("GENRE_ID"),
                        rs.getString("Genre_name")
                );
                return genre;
            }
        });
        Set<Genre> genreFromImport = new HashSet<>(genres);

        return genreFromImport;
    }
}
