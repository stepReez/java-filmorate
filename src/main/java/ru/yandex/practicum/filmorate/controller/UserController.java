package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private static int idCounter = 1;
    List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if(user.getEmail().isEmpty()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if(!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать @");
        }
        if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым или содержать пробелы");
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if( user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(idCounter);
        users.add(user);
        idCounter++;
        log.info("Пользователь создан успешно");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if(user.getEmail().isEmpty()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if(!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать @");
        }
        if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым или содержать пробелы");
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if(user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.size() >= user.getId()) {
            users.set(user.getId() - 1, user);
            log.info("Пользователь обновлен успешно");
            return user;
        } else {
            throw new ValidationException("Пользователя с таким id не существует");
        }

    }
}
