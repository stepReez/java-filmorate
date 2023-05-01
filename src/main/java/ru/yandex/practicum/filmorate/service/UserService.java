package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int firstId, int secondId) {
        if (firstId > 0 && secondId > 0) {
            userStorage.addFriend(firstId, secondId);
        } else {
            throw new NotFoundException("Id пользователя должно быть положительным");
        }
    }

    public void removeFriend(int firstUser, int secondUser) {
        if (firstUser > 0 && secondUser > 0) {
            userStorage.deleteFriend(firstUser, secondUser);
        } else {
            throw new NotFoundException("Id пользователя должно быть положительным");
        }

    }

    public List<User> getMutualFriends(int firstUser, int secondUser) {
        List<User> mutualFriends = new ArrayList<>();
        Set<Integer> setFriends = userStorage.findUser(secondUser).getFriends();

        for (Integer friend : userStorage.findUser(firstUser).getFriends()) {
            if (setFriends.contains(friend)) {
                mutualFriends.add(userStorage.findUser(friend));
            }
        }
        log.info("Выведен список общих друзей юзера " + firstUser + " и " + secondUser);
        return mutualFriends;
    }

    public List<User> getAllFriends(int id) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.findUser(id);

        for (Integer friend : user.getFriends()) {
            friends.add(userStorage.findUser(friend));
        }
        log.info("Выведен список всех друзей " + id + " юзера");
        return friends;
    }
}
