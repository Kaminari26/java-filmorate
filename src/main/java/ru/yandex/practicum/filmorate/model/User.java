package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    private int id;

    //@NoSpaces(message = "login не должен содержать пробелы")
    @NotBlank
    private String login;

    private String name;
    @NotBlank
    @Email
    private String email;

    @Past
    private LocalDate birthday;
}
