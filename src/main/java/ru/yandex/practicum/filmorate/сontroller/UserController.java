package ru.yandex.practicum.filmorate.сontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
//@Component
@Service
public class UserController {
    InMemoryUserStorage inMemoryUserStorage;
    UserService userservice;
    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userservice) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userservice = userservice;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Пришел запрос Post /users");
        inMemoryUserStorage.add(user);
        log.info("Отправлен ответ" + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Пришел запрос Put /users");
        User updatedUser = inMemoryUserStorage.update(user);
        log.info("Отправлен ответ" + updatedUser);
        return updatedUser;
        }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пришел запрос Put /{id}/friends/{friendId}");
        User user = userservice.addFriends(id, friendId);
        log.info("Отправлен ответ" + user);
        return user;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пришел запрос Delete /{id}/friends/{friendId}");
        User user = userservice.deleteFriends(id, friendId);
        log.info("Отправлен ответ" + user);
        return user;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> friendsList(@PathVariable Long id) {
        log.info("Пришел запрос Get /{id}/friends");
        Collection<User> users = userservice.friendsListUsers(id);
        log.info("Отправлен ответ" + users);
        return users;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Пришел запрос Get /{id}/friends/common/{otherId}");
        Collection<User> users =  userservice.mutualFriends(id,otherId);;
        log.info("Отправлен ответ" + users);
        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Пришел запрос Get /{id}");
        User user = inMemoryUserStorage.getById(id);
        log.info("Отправлен ответ" + user);
        return user;
    }

    @GetMapping
    public Collection<User> getUsersList() {
        log.info("Пришел запрос Get /users");
        Collection<User> users = inMemoryUserStorage.getUsersList();
        log.info("Отправлен ответ" + users);
        return users;
    }
}
