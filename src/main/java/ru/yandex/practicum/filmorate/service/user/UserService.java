package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
     InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriends (Long id , Long friendId) {
        if(inMemoryUserStorage.getUsersMap().get(id) == null || inMemoryUserStorage.getUsersMap().get(friendId) == null){
            log.error("Пользователь не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Пользователь " +id+ " добавляет в друзья " + friendId);
        inMemoryUserStorage.getUsersMap().get(id).getFriends().add(friendId);
        inMemoryUserStorage.getUsersMap().get(friendId).getFriends().add(id);
        return inMemoryUserStorage.getUsersMap().get(id);
    }

    public User deleteFriends (Long id, Long friendId) {
        if(inMemoryUserStorage.getUsersMap().get(id) == null || inMemoryUserStorage.getUsersMap().get(friendId) == null){
            log.error("Пользователь не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("Пользователь " +id+ " удаляет из друзей " + friendId);
        inMemoryUserStorage.getUsersMap().get(id).getFriends().remove(friendId);
        inMemoryUserStorage.getUsersMap().get(friendId).getFriends().remove(id);
        return inMemoryUserStorage.getUsersMap().get(id);
    }

    public Collection<User> mutualFriends(Long id, Long otherId) {
       if (inMemoryUserStorage.getUsersMap().get(id) == null || inMemoryUserStorage.getUsersMap().get(otherId) == null) {
        log.warn("Пользователь не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
       Set<Long> mutualFriendsId = inMemoryUserStorage.getUsersMap().get(id).getFriends().stream().
               filter(inMemoryUserStorage.getUsersMap().get(otherId).getFriends():: contains).collect(Collectors.toSet());
       ArrayList<User> mutualFriends = new ArrayList<>();
       for (Long friendsId : mutualFriendsId) {
           mutualFriends.add(inMemoryUserStorage.getUsersMap().get(friendsId));
       }
       return mutualFriends;
    }

    public Collection<User> friendsListUsers(Long id) {
        Collection<User> users = new ArrayList<>();
        for(Long friends : inMemoryUserStorage.getById(id).getFriends()) {
            users.add(inMemoryUserStorage.getById(friends));
        }
        return users;
    }
    private boolean contains(Long id) {
        return inMemoryUserStorage.getUsersMap().containsKey(id);
    }
}
