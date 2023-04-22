package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "getInstance")
@NoArgsConstructor
public class Genre {

    private int id;

    private String name;
}
