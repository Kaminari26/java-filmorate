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
    Validation validation = new Validation();
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping(value = "/post")
    public User createUser(@RequestBody User user) throws ValidationException {
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " добавлен.");
        return user;
    }

    @PostMapping(value = "/name")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
        log.info("Пользователь " + user.getName() + " обновлен.");
        return user;
    }

    @GetMapping(value = "/users")
    public Map getUsersList(){
        return  users;
    }
}
