package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Repository
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM Genre WHERE genre_id = ?", id);
        if (!userRows.next()) {
            return null;
        }

        Genre genre = Genre.getInstance(
                userRows.getInt("genre_id"),
                userRows.getString("name")
        );
        return genre;
    }

    @Override
    public Collection<Genre> getAll() {
        Collection<Genre> genres = jdbcTemplate.query("SELECT * FROM GENRE g order by g.genre_id", (rs, rowNum) -> {
            Genre genre = Genre.getInstance(
                    rs.getInt("genre_id"),
                    rs.getString("name")
            );
            return genre;
        });

        return genres;
    }

    @Override
    public Set<Genre> getGenresByFilmId(Long id) {
        String sql = "SELECT  g.* " +
                "FROM GENRE g " +
                "JOIN FILM_GENRE fg ON g.GENRE_ID = fg.GENRE_ID " +
                "JOIN FILMS f ON f.FILM_ID = fg.FILM_ID WHERE f.FILM_ID = ?" +
                "order by g.GENRE_ID";

        Collection<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = Genre.getInstance(
                    rs.getInt("GENRE_ID"),
                    rs.getString("NAME")
            );

            return genre;
        }, id);

        if (genres.size() == 0) {
            return null;
        }

        Comparator<Genre> comparator = Comparator.comparing(genre -> genre.getId());
        Set<Genre> sortedGenresByIdAsc = new TreeSet<>(comparator);
        sortedGenresByIdAsc.addAll(genres);

        return sortedGenresByIdAsc;
    }

    @Override
    public void add(Set<Genre> genres, Long filmId) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE film_id = ?", filmId);

        if (genres == null || genres.size() == 0) {
            return;
        }

        Iterator<Genre> genresItter = genres.iterator();
        String insertMultipleGenresQuery = "INSERT INTO FILM_GENRE(film_id, genre_id) VALUES(?,?)";
        jdbcTemplate.batchUpdate(insertMultipleGenresQuery, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = genresItter.next();
                ps.setLong(1, filmId);
                ps.setLong(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }
}
