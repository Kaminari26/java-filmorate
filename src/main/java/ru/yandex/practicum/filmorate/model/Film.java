package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    private int id;

    @NotBlank
    private String name;

    @Size (min = 1 ,max = 200)
    private String description;
    //  @ReleaseDate(message = "Неверно указана дата")
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
