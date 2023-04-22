package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MemoryStorage;

import javax.validation.Valid;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements MemoryStorage<User> {
    private Long counter = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь " + user.getName() + "не был обновлен.");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        if (user.getName() == null) {
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
        log.info("Запрошен пользователь с {} ", id);
        User user = users.get(id);
        if (user == null) {
            log.error("Пользователь не найден");
            throw new NullPointerException("Не найден пользователь для удаления");
        }
        log.info("Пользователь найден: {}", user);
        return user;
    }

    @Override
    public Map<Long, User> getMap() {
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
        User user = users.get(id);
        if (user == null) {
            log.warn("Пользователь " + id + " не найден");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        log.info("Удаление пользователя" + id);
        users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
