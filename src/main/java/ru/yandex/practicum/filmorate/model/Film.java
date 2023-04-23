package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor(staticName = "getInstance")
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    private Date releaseDate;

    @Positive
    private int duration;

    private MpaRating mpa;

    private Set<Genre> genres;
}
