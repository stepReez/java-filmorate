package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"users\" WHERE \"id\" = ?", id);
        if (userRows.next()) {
            log.info("Найден пользователь: {}", userRows.getInt("id"));
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(),
                    findAllConfirmedFriends(id).get()
            );
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    public Optional<Set<Integer>> findAllConfirmedFriends(int id) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from \"friends\" where \"user_id\" = ?", id);
        Set<Integer> friends = new TreeSet<>();
        while (friendRows.next()) {
            friends.add(friendRows.getInt("friend_id"));
        }
        return Optional.of(friends);
    }

    @Override
    public Optional<User> createUser(User user) {
        String sqlQuery = "insert into \"users\" (\"id\", \"email\", \"login\", \"name\", \"birthday\") " +
                "values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        return findUserById(user.getId());
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery = "update \"users\" set " +
                "\"email\" = ?, \"login\" = ?, \"name\" = ?, \"birthday\" = ? " +
                "where \"id\" = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return findUserById(user.getId());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into \"friends\" (\"user_id\", \"friend_id\", \"status\") values (?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                userId,
                friendId,
                false);
    }

    @Override
    public Optional<Boolean> confirmFriend(int userId, int friendId) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * " +
                        "from \"friends\" where \"user_id\" = ? and \"friend_id\" = ?",
                friendId, userId);
        if (friendRows.next()) {
            jdbcTemplate.update("update \"friends\" set \"status\" = ? " +
                    "where \"user_id\" = ? and \"friend_id\" = ?", true, userId, friendId);
            return Optional.of(true);
        } else {
            return Optional.of(false);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        SqlRowSet friendRow1 = jdbcTemplate.queryForRowSet("select * " +
                        "from \"friends\" where \"user_id\" = ? and \"friend_id\" = ?",
                friendId, userId);

        SqlRowSet friendRow2 = jdbcTemplate.queryForRowSet("select * " +
                        "from \"friends\" where \"user_id\" = ? and \"friend_id\" = ?",
                userId, friendId);

        if (friendRow1.next()) {
            jdbcTemplate.update("DELETE FROM \"friends\" WHERE \"user_id\" = ? and \"friend_id\" = ?",
                    friendId, userId);
        } else if (friendRow2.next()) {
            jdbcTemplate.update("DELETE FROM \"friends\" WHERE \"user_id\" = ? and \"friend_id\" = ?",
                    userId, friendId);
        }
    }

    @Override
    public Optional<List<User>> findAllUsers(String count) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"users\" LIMIT ?", count);

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(),
                    findAllConfirmedFriends(userRows.getInt("id")).get()
            );

            users.add(user);
        }
        return Optional.of(users);
    }
}