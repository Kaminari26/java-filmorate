package ru.yandex.practicum.filmorate.сontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userservice = new UserService(inMemoryUserStorage);

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.update(user);
            return user;
        }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
       return userservice.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable Long id, @PathVariable Long friendId) {
       return userservice.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> friendsList(@PathVariable Long id) {
       return userservice.friendsListUsers(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userservice.mutualFriends(id,otherId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return inMemoryUserStorage.getById(id);
    }

    @GetMapping
    public Collection<User> getUsersList() {
        return inMemoryUserStorage.getUsersList();
    }
}
