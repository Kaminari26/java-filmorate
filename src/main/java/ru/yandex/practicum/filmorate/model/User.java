package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.*;
import java.sql.Date;
import java.time.LocalDate;
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
