package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class User {
    int id;
    @NonNull
    String email;
    String login;
    String name;
    LocalDateTime birthday;
}
