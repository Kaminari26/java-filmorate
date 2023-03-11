package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    int id;

    @NotNull
    String name;

    @Size (max = 200)
    String description;

   // @ReleaseDate(message = "Неверно указана дата")
    LocalDate releaseDate;

    @Positive
    int duration;

    public int filmId() {
     return id+=+1;
    }
}
