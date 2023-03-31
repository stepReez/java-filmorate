package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private static int idCounter = 1;
    private static List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public User createUser(User user) {
        validate(user);
        user.setId(idCounter);
        users.add(user);
        idCounter++;
        log.info("Пользователь создан успешно");
        return user;
    }

    public User updateUser(User user) {
        validate(user);
        if (users.size() >= user.getId()) {
            users.set((user.getId() - 1), user);
            log.info("Пользователь обновлен успешно");
            return user;
        } else {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }

    public User findUser(int id) {
        if (users.size() >= id) {
            return users.get(id - 1);
        } else {
            throw new NotFoundException("Фильма с таким id не существует");
        }
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
