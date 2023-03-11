package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class Film {
   final LocalDate localDate = LocalDate.of(1895, 12, 28);
    int id;

    @NonNull
    String name;
    @Size (max = 200)
    String description;
    @ReleaseDate(message = "Неверно указана дата")
    LocalDate releaseDate;
    @Positive
    int duration;
}
