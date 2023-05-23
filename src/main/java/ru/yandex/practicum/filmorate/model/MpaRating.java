package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "getInstance")
@NoArgsConstructor
@Data
public class MpaRating {

    private int id;

    private String name;
}
