package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    @NoSpaces(message = "login не должен содержать пробелы")
    @NotNull
    String login;
    String name;
    @NotNull
    @Email
    String email;
    @Past
    LocalDate birthday;
}
