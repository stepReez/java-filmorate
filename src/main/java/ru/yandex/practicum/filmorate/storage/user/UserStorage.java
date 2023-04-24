package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    User findUser(int id);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);
}
