package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    int id;

    @NotBlank
    @NotNull
    String name;

    @Size (max = 200)
    String description;
    //  @ReleaseDate(message = "Неверно указана дата")
    LocalDate releaseDate;

    @Positive
    int duration;

    public int filmId() {
     return id+=+1;
    }
}
