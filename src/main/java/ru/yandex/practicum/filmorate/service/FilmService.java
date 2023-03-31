package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int user) {
        if (filmId < 0) {
            throw new NotFoundException("Id фильма должно быть положительным");
        } else if (user < 0) {
            throw new NotFoundException("Id пользователя должно быть положительным");
        } else {
            filmStorage.findFilm(filmId).addLike(user);
            log.info("Юзер " + user + " поставил лайк фильму " + filmId);
        }
    }

    public void removeLike(int filmId, int user) {
        if (filmId < 0) {
            throw new NotFoundException("Id фильма должно быть положительным");
        } else if (user < 0) {
            throw new NotFoundException("Id пользователя должно быть положительным");
        } else {
            filmStorage.findFilm(filmId).removeLike(user);
            log.info("Юзер " + user + " убрал лайк с фильма " + filmId);
        }
    }

    public List<Film> getTop(String count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        List<Film> list = filmStorage.getFilms()
                .stream()
                .sorted(comparator.reversed())
                .limit(Integer.parseInt(count))
                .collect(Collectors.toList());
        log.info("Список лучших " + count + " фильмов успешно выведен");
        return list;
    }
}
