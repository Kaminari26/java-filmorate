package ru.yandex.practicum.filmorate.сontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int counter;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрошено добавление пользователя " + user);
        user.setId(++counter);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " добавлен.");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь " + user.getName() + "не был обновлен.");
            throw new RuntimeException("Не удалось обновить пользователя");
        }
            if(user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getName() + " обновлен.");
            return user;
        }

    @GetMapping
    public Collection<User> getUsersList() {
        log.info("Запрошен список пользователей");
        return users.values();
    }
}
