package ru.yandex.practicum.filmorate.сontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        user.setId(user.generateId());
        users.put(user.getLogin(), user);
        log.info("Пользователь " + user.getName() + " добавлен.");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getLogin())) {
            users.put(user.getLogin(), user);
        }
        log.info("Пользователь " + user.getName() + " обновлен.");
        return user;
    }

    @GetMapping
    public Map getUsersList(){
        return  users;
    }
}
