package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao {
    Optional<User> findUserById(int id);

    Optional<Set<Integer>> findAllConfirmedFriends(int id);

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    void addFriend(int userId, int friendId);

    Optional<Boolean> confirmFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Optional<List<User>> findAllUsers();
}
