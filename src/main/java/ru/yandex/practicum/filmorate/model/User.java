package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    int id = 0 ;

    //@NoSpaces(message = "login не должен содержать пробелы")
    @NotNull
    String login;

    String name;
    @NotBlank
    @NotNull
    @Email
    String email;

    @Past
    LocalDate birthday;
}
