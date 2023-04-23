package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

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
    public MpaRating getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE mpa_id = ?", id);
        if (!userRows.next()) {
            return null;
        }

        MpaRating mpaRating = MpaRating.getInstance(
                userRows.getInt("mpa_id"),
                userRows.getString("name")
        );

        return mpaRating;
    }

    @Override
    public Collection<MpaRating> getRatingAll() {
        Collection<MpaRating> mpaRatings = jdbcTemplate.query("SELECT * FROM MPA m order by m.mpa_id", (rs, rowNum) -> {
            MpaRating mpaRating = MpaRating.getInstance(
                    rs.getInt("mpa_id"),
                    rs.getString("name")
            );

            return mpaRating;
        });

        return mpaRatings;
    }
}
