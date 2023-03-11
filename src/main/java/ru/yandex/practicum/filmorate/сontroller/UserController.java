package ru.yandex.practicum.filmorate.сontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validationExeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    int userId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        user.setId(userId);
        if (user.getName() == null){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " добавлен.");
        userId +=1;
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            if(user.getName() == null){
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getName() + " обновлен.");
            return user;

        }
        log.info("Пользователь " + user.getName() + "не был обновлен.");
        throw new RuntimeException("500");
    }

    @GetMapping
    public Collection<User> getUsersList(){
        return users.values();
    }
}
