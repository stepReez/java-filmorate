package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private static int idCounter = 1;
    private static List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validate(user);
        user.setId(idCounter);
        users.add(user);
        idCounter++;
        log.info("Пользователь создан успешно");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validate(user);
        if (users.size() >= user.getId()) {
            users.set(user.getId() - 1, user);
            log.info("Пользователь обновлен успешно");
            return user;
        } else {
            throw new ValidationException("Пользователя с таким id не существует");
        }
    }

    private void validate(User user) {
        if(user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if( user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
