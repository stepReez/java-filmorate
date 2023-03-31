package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    public List<User> getUsers();
    public User createUser(User user);
    public User updateUser(User user);
    public User findUser(int id);
}
