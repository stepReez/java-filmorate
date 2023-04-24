package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int user) {
        if (filmId < 0) {
            throw new NotFoundException("Id фильма должно быть положительным");
        } else if (user < 0) {
            throw new NotFoundException("Id пользователя должно быть положительным");
        } else {
            filmStorage.addLike(filmId, user);
        }
    }

    public void removeLike(int filmId, int user) {
        if (filmId < 0) {
            throw new NotFoundException("Id фильма должно быть положительным");
        } else if (user < 0) {
            throw new NotFoundException("Id пользователя должно быть положительным");
        } else {
            filmStorage.removeLike(filmId, user);
        }
    }

    public List<Film> getTop(String count) {
        List<Film> top = filmStorage.getTop(count);
        log.info("Список лучших " + count + " фильмов успешно выведен");
        return top;
    }
}
