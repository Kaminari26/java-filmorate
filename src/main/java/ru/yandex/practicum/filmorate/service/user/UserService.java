package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
     InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
         this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriends (Long id , Long friendId) {
        if(inMemoryUserStorage.getUsersMap().get(id) == null || inMemoryUserStorage.getUsersMap().get(friendId) == null){
            log.error("Пользователь не найден");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }

        log.info("Пользователь " +id+ " добавляет в друзья " + friendId);
        inMemoryUserStorage.getUsersMap().get(id).getFriendsIds().add(friendId);
        inMemoryUserStorage.getUsersMap().get(friendId).getFriendsIds().add(id);
        log.info("Друзья добавлены");
        return inMemoryUserStorage.getUsersMap().get(id);
    }

    public User deleteFriends (Long id, Long friendId) {
        if(inMemoryUserStorage.getUsersMap().get(id) == null || inMemoryUserStorage.getUsersMap().get(friendId) == null){
            log.error("Пользователь не найден");
            throw new NoSuchElementException("Не удалось найти пользователя");
        }
        log.info("Пользователь " +id+ " удаляет из друзей " + friendId);
        inMemoryUserStorage.getUsersMap().get(id).getFriendsIds().remove(friendId);
        inMemoryUserStorage.getUsersMap().get(friendId).getFriendsIds().remove(id);
        log.info("Друг удален");
        return inMemoryUserStorage.getUsersMap().get(id);
    }

    public Collection<User> mutualFriends(Long id, Long otherId) {
       if (inMemoryUserStorage.getUsersMap().get(id) == null || inMemoryUserStorage.getUsersMap().get(otherId) == null) {
        log.error("Пользователь не найден");
           throw new NoSuchElementException("Не удалось найти пользователя");
        }
       Set<Long> mutualFriendsId = inMemoryUserStorage.getUsersMap().get(id).getFriendsIds().stream().
               filter(inMemoryUserStorage.getUsersMap().get(otherId).getFriendsIds():: contains).collect(Collectors.toSet());
       ArrayList<User> mutualFriends = new ArrayList<>();
       for (Long friendsId : mutualFriendsId) {
           mutualFriends.add(inMemoryUserStorage.getUsersMap().get(friendsId));
       }
       log.info("Список общих друзей получен");
       return mutualFriends;
    }

    public Collection<User> friendsListUsers(Long id) {
        Collection<User> users = new ArrayList<>();
        for(Long friends : inMemoryUserStorage.getById(id).getFriendsIds()) {
            users.add(inMemoryUserStorage.getById(friends));
        }
        log.info("Список друзей получен");
        return users;
    }
    private boolean contains(Long id) {
        return inMemoryUserStorage.getUsersMap().containsKey(id);
    }
}
