package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserDao userDao;

    private static int idCounter = 1;

    public UserDbStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getUsers(String count) {
        return userDao.findAllUsers(count).get();
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(idCounter);
        idCounter++;
        return userDao.createUser(user).get();
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        if (user.getId() > idCounter) {
            throw new NotFoundException(String.format("Пользователя с id %d не существует", user.getId()));
        }
        return userDao.updateUser(user).get();
    }

    @Override
    public User findUser(int id) {
        try {
            return userDao.findUserById(id).get();
        } catch (NoSuchElementException e) {
               throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userDao.confirmFriend(userId, friendId).get()) {
            log.info("Юзеры " + userId + " и " + userId + " теперь друзья");
        } else {
            userDao.addFriend(userId, friendId);
            log.info(String.format("Пользователь %d отправил заявку в друзья пользователю %d", userId, friendId));
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        userDao.deleteFriend(userId, friendId);
        log.info("Юзеры " + userId + " и " + friendId + " больше не друзья");
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}
