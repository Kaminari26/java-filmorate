package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor(staticName = "getInstance")
public class User {

    private Long id;

    private Set<Long> friendsIds = new HashSet<>();

    @NotBlank
    private String login;

    private String name;

    @NotBlank
    @Email
    private String email;

    @Past
    private Date birthday;
}
