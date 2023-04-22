package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
        Date MOVIE_BIRTHDAY = Date.valueOf(LocalDate.of(1895,12,28));
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

            if (film.getReleaseDate().before(MOVIE_BIRTHDAY)){
                log.error("Неверно указана дата релиза " + film.getName());
                throw new ValidationException("Произошла ошибка при попытке создания фильма");
            }
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

        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE film_id = ?", film.getId());

        if(film.getGenres() == null || film.getGenres().size() == 0)
        {
            return film;
        }

        Comparator<Genre> comparator = Comparator.comparing(obj -> obj.getId());

        Iterator<Genre> genres = film.getGenres().iterator();

        Set<Genre> sortedGenresByIdAsc = new TreeSet<>(comparator);



        String insertMultipleGenresQuery = "INSERT INTO FILM_GENRE(film_id, genre_id) VALUES(?,?)";

        jdbcTemplate.batchUpdate(insertMultipleGenresQuery, new BatchPreparedStatementSetter(){

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = genres.next();
                ps.setLong(1,film.getId());
                ps.setLong(2,genre.getId());
                sortedGenresByIdAsc.add(genre);
            }

            @Override
            public int getBatchSize() {
                return film.getGenres().size();
            }
        });

        film.setGenres(sortedGenresByIdAsc);

        return film;

    }

    @Override
    public Film getById(Long id) {
        String sql = "SELECT f.* , m.NAME AS mpa_name FROM FILMS f  LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID WHERE f.film_Id = "+id+";";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql);
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
                MpaRating.getInstance(userRows.getInt("MPA_ID"), userRows.getString("MPA_NAME")),
                getGenreSets(id)
                );
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
                        MpaRating.getInstance(rs.getInt("mpa_id"),rs.getString("MPA_name")),
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
        Film film = getById(id);
         film.getLikes().add(userId);
         update(film);
         String sql = "MERGE INTO LIKES_FILM (film_id, user_id)" +
        " values(?,?)";
         jdbcTemplate.update(sql, id, userId);
        return film;
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        SqlRowSet likesFilmSet = jdbcTemplate.queryForRowSet("SELECT * FROM LIKES_FILM WHERE film_id = ? AND user_id = ?",id,userId);
        if(!likesFilmSet.next())
        {
            throw new NoSuchElementException("Не удалось найти ");
        }

        jdbcTemplate.update("DELETE FROM LIKES_FILM WHERE film_id = ? AND user_id = ?", id, userId);
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
                "LEFT JOIN MPA AS ap ON  f.MPA_ID = AP .MPA_ID " +
                "GROUP BY f.FILM_ID " +
        "ORDER BY COUNT(lf.FILM_ID) DESC \n" +
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
                    MpaRating.getInstance(rs.getInt("mpa_id"),rs.getString("MPA_name")),
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
        String sql = "SELECT  g.* " +
                "FROM GENRE g " +
                "JOIN FILM_GENRE fg ON g.GENRE_ID = fg.GENRE_ID " +
                "JOIN FILMS f ON f.FILM_ID = fg.FILM_ID WHERE f.FILM_ID = ?" +
                "order by g.GENRE_ID";
        Collection<Genre> genres = jdbcTemplate.query(sql, new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genre genre = Genre.getInstance(
                        rs.getInt("GENRE_ID"),
                        rs.getString("NAME")
                );
                return genre;
            }
        }, id);

        Set<Genre> genreSortedSet = new HashSet<>(genres);
        return genreSortedSet;
    }
}
