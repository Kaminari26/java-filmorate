package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Long counter = 0l;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User update(@Valid @RequestBody User user) {
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

    public Collection<User> getUsersList() {
        log.info("Запрошен список пользователей");
        return users.values();
    }

    @Override
    public User getById(Long id) {
        log.info("Запрошен пользователь с id " + id);
        if(!contains(id)) {
            log.error("Пользователь не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else {
            log.info("Пользователь найден");
            return users.get(id);
        }
    }

    public Map<Long, User> getUsersMap() {
        return users;
    }

    @Override
    public User add(User user) {
        log.info("Запрошено добавление пользователя " + user);
        user.setId(++counter);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " добавлен.");
        return user;
    }

    @Override
    public void delete(Long id) {
        if (!contains(id)) {
            log.warn("Пользователь " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }else {
            log.info("Удаление пользователя" + id);
            users.remove(id);
        }
    }

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Boolean contains(Long id) {
        return users.containsKey(id);
    }
}
