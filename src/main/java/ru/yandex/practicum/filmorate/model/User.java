package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.sql.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor(staticName = "getInstance")
public class User {

    private Long id;

    @NotBlank
    private String login;

    private String name;

    @NotBlank
    @Email
    private String email;

    @Past
    private Date birthday;
}
