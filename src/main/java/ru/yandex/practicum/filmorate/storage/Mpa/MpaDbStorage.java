package ru.yandex.practicum.filmorate.storage.Mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Primary
@Repository
@Slf4j
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getRatingById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE mpa_id = ?", id);
        if (!userRows.next()) {
            throw new NoSuchElementException("Не удалось найти рейтинг");
        }

        MpaRating mpaRating = MpaRating.getInstance(
                userRows.getInt("mpa_id"),
                userRows.getString("name")
        );
        return mpaRating;
    }


    @Override
    public MpaRating getRating(int id) {
        return null;
    }

    public List<MpaRating> getRatingAll() {
        return null;
    }

    public Collection<MpaRating> getByQuery(String sqlQuery, Object... params) {
        return null;
    }

    @Override
    public MpaRating add(MpaRating obj) {
        return null;
    }

    @Override
    public MpaRating update(MpaRating obj) {
        return null;
    }

    @Override
    public MpaRating getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE mpa_id = ?", id);
        if (!userRows.next()) {
            throw new NoSuchElementException("Не удалось найти рейтинг");
        }

        MpaRating mpaRating = MpaRating.getInstance(
                userRows.getInt("mpa_id"),
                userRows.getString("name")
        );
        return mpaRating;
    }

    @Override
    public void delete(Long id) {

    }

    public Collection<MpaRating> getAll() {
        Collection<MpaRating> mpaRatings = jdbcTemplate.query("SELECT * FROM MPA m order by m.mpa_id", new RowMapper<MpaRating>() {
            @Override
            public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
                MpaRating mpaRating = MpaRating.getInstance(
                        rs.getInt("mpa_id"),
                        rs.getString("name")
                );
                return mpaRating;
            }
        });

        return mpaRatings;
    }
}
