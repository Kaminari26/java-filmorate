package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.*;
import java.time.LocalDate;;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    private Set<Long> likeList = new HashSet<>();

    private Long id;

    @NotBlank
    private String name;

    @Size (min = 1 ,max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    public int getAmountOfLikes() {
       return likeList.size();
    }
}
